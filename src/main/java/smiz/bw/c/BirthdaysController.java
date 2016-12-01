package smiz.bw.c;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smiz.bw.dto.Celebrant;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by smiz on 30/11/16.
 */
@RestController
public class BirthdaysController {


	private static final String FIELD_PROC_ID = "procId";
	private static final String FIELD_STATUS = "status";
	private static final String FIELD_PERSONS = "persons";
	private static final String STATUS_DONE = "done";
	private static final String STATUS_PENDING = "pending";

	private final AtomicLong procCounter = new AtomicLong();
	@RequestMapping("/birthdays")
	public Map<String, Object> getByMonth(@RequestParam(value = "m", required = false) Integer month) {

		if (month == null) {
			month = LocalDate.now().getMonthValue();
		}

		// TODO initiate task
		// ...

		return Collections.singletonMap(FIELD_PROC_ID, procCounter.incrementAndGet());
	}

	@RequestMapping("/birthdays/{procId}")
	public Map<String, Object> getResult(@PathVariable Long procId) {

		boolean stillInProcess = true;

		if (stillInProcess) {
			return Collections.singletonMap("status", STATUS_PENDING);
		} else {
			Map<String, Object> res = new HashMap<>();
			res.put(FIELD_STATUS, STATUS_DONE);
			res.put(FIELD_PERSONS, Collections.singletonList(new Celebrant("John Doe", 23)));
			return res;
		}
	}

}
