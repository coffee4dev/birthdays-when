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
 * Class used as Spring Service calculating celebrants of certain month among known persons.
 */
@Service
public class BirthdayService {

	@Autowired
	PersonRepository personRepository;

	@Autowired
	DaysCalculator daysCalculator;

	/**
	 * Returns java.util.List of celebrants who have birthdays on month specified, with days left until the birthday of each one.
	 * @param month month on which persons to be returned must have birthday
	 * @return List of persons having birthdays in month specified, along with days left until the birthday.
	 */
	public List<Celebrant> getCelebrantsForMonth(int month) {

		return personRepository.findByMonthOfBirth(month)
				.map(p -> new Celebrant(p.getName(), daysCalculator.getDaysLeftUntil(p.getMonthOfBirth(), p.getDayOfBirth())))
				.collect(Collectors.toList());
	}
}
