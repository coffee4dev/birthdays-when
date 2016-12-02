package smiz.bw.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import smiz.bw.dto.Celebrant;
import smiz.bw.dto.Result;
import smiz.bw.repo.PersonRepository;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by smiz on 30/11/16.
 */
@RestController
@Slf4j
public class BirthdaysController {

	private static final String FIELD_PROC_ID = "procId";
	private static final String FIELD_STATUS = "status";
	private static final String FIELD_PERSONS = "persons";
	public static final int ONE_SECOND = 1000;

	@Autowired
	private PersonRepository personRepository;

	@Autowired
	private BirthdayService birthdayService;

	private final ConcurrentMap<Long, Result> results = new ConcurrentHashMap<>();

	private final AtomicLong procCounter = new AtomicLong();

	@Autowired
	private TaskExecutor taskExecutor;

	@Scheduled(fixedRate = 60 * ONE_SECOND)
	public void evictObsoleteResults() {

	}

	@RequestMapping("/birthdays")
	public Map<String, Object> getByMonth(@RequestParam(value = "m", required = false) final Integer month) {

		LocalDate now = LocalDate.now();
		final int m = month == null ? now.getMonthValue() : month;

		final long procId = procCounter.incrementAndGet();
		results.put(procId, new Result());

		// initiate task
		taskExecutor.execute(() -> {
			List<Celebrant> celebrants = birthdayService.getCelebrantsForMonth(m);

			try {
				Thread.sleep(2 * ONE_SECOND);
			} catch (InterruptedException e) {
				log.warn("Process {}: execution interrupted", procId);
			} finally {
				Result r = results.getOrDefault(procId, new Result());
				r.setStatus(Result.STATUS_DONE);
				r.setPersons(celebrants);
			}
		});

		return Collections.singletonMap(FIELD_PROC_ID, procId);
	}

	@RequestMapping("/birthdays/{procId}")
	public Result getResult(@PathVariable Long procId) {
		Result result = results.get(procId);
		if (result == null) {
			throw new ResourceNotFoundException();
		}
		if (result.isDone()) {
			results.remove(procId);
		}
		return result;
	}

	/**
	 * TODO test purposes, remove
	 * @return
	 */
	@RequestMapping("/birthdays/all")
	public List<Map<String, Object>> getAll() {
		return StreamSupport.stream(personRepository.findAll().spliterator(), true)
				.map(p ->  {
					Map<String, Object> map = new HashMap<>();
					map.put("name", p.getName());
					map.put("mob", p.getMonthOfBirth());
					map.put("dob", p.getDayOfBirth());

					return map;
				})
				.collect(Collectors.toList());
	}

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public class ResourceNotFoundException extends RuntimeException {

	}

}
