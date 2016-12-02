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

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Class used as Spring REST Controller bean.
 * Handles requests, stores results and evicts obsolete ones.
 */
@RestController
@Slf4j
public class BirthdaysController {

	private static final String FIELD_PROC_ID = "procId";

	private static final int ONE_SECOND_IN_MSEC = 1000;
	private static final int THREAD_SLEEP_SEC = 5 * ONE_SECOND_IN_MSEC;
	private static final int RESULT_EVICTION_TIMEOUT_SEC = 15;

	private BirthdayService birthdayService;

	private TaskExecutor taskExecutor;

	private final ConcurrentMap<Long, Result> results = new ConcurrentHashMap<>();
	private final AtomicLong procCounter = new AtomicLong();

	public BirthdaysController(BirthdayService birthdayService, TaskExecutor taskExecutor) {
		this.birthdayService = birthdayService;
		this.taskExecutor = taskExecutor;
	}

	/**
	 * Goes through all results stored and removes ones that are obsolete.
	 */
	@Scheduled(fixedRate = RESULT_EVICTION_TIMEOUT_SEC * ONE_SECOND_IN_MSEC)
	public void evictObsoleteResults() {
		log.debug("Obsolete results eviction started");
		final int[] count = new int[]{0};

		final Instant now = Instant.now();
		results.keySet().removeIf(procId -> {
			boolean evict = Duration.between(results.get(procId).getCreatedAt(), now).getSeconds() > RESULT_EVICTION_TIMEOUT_SEC;
			count[0] += evict ? 1 : 0;
			return evict;
		});

		log.debug("Evicted {} obsolete results", count[0]);
	}

	/**
	 * Handles initial request to get users who have birthdays in a month specified.
	 * @param month a month in which users to be returned are having birthdays; if none is specified, current month is assumed.
	 * @return java.util.Map containing single pair: key is a constant string field identifier,
	 * 		and value is an identificator of a process by which result is to be retrieved later.
	 */
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
				Thread.sleep(THREAD_SLEEP_SEC);
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

	/**
	 * Handles request for result of processing specified by process Id.
	 * @param procId Id of a process result of which is to be retrieved.
	 * @return Result object containing status of processing and java.util.Jist of celebrants,
	 * 		depending on the status:
	 * 	<ul>
	 * 		<li>if status is "pending", celebrants list is empty</li>
	 * 		<li>if status is "done", celebrants list is populated with persons who have birthdays in month specified
	 * 				when an initial request was made</li>
	 * 	</ul>
	 */
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

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	private class ResourceNotFoundException extends RuntimeException {
	}

}
