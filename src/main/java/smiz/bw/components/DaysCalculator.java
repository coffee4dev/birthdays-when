package smiz.bw.components;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Service class calculating days left until certain day of certain month.
 */
@Service
public class DaysCalculator {

	/**
	 * Calculates number of days left until specified day of specified month.
	 * @param month a month until day of which days left are to be calculated
	 * @param day a day of month specified by corresponding parameter month until which days left are to be calculated
	 * @return number of days left until specified day of specified month.
	 */
	public long getDaysLeftUntil(int month, int day) {
		LocalDate now = LocalDate.now();
		LocalDate then = LocalDate.of((month < now.getMonthValue()
				|| (month == now.getMonthValue() && day < now.getDayOfMonth()))
						? now.getYear() + 1 : now.getYear(), month, day);
		return ChronoUnit.DAYS.between(now, then);
	}

}
