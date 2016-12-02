package smiz.bw.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import smiz.bw.dto.Celebrant;
import smiz.bw.model.Person;
import smiz.bw.repo.PersonRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by smiz on 01/12/16.
 */
@Service
public class BirthdayService {

	@Autowired
	PersonRepository personRepository;

	@Autowired
	DaysCalculator daysCalculator;

	public List<Celebrant> getCelebrantsForMonth(int month) {

		return personRepository.findByMonthOfBirth(month)
				.map(p -> new Celebrant(p.getName(), daysCalculator.getDaysLeftUntil(p.getMonthOfBirth(), p.getDayOfBirth())))
				.collect(Collectors.toList());
	}
}
