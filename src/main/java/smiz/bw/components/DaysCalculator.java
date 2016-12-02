package smiz.bw.components;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Created by smiz on 01/12/16.
 */
@Service
public class DaysCalculator {

	public long getDaysLeftUntil(int month, int day) {
		LocalDate now = LocalDate.now();
		LocalDate then = LocalDate.of((month < now.getMonthValue()
				|| (month == now.getMonthValue() && day < now.getDayOfMonth()))
						? now.getYear() + 1 : now.getYear(), month, day);
		return ChronoUnit.DAYS.between(now, then);
	}

}
