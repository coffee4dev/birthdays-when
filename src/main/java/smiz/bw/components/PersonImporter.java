package smiz.bw.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import smiz.bw.conf.Application;
import smiz.bw.model.Person;
import smiz.bw.repo.PersonRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class used as Spring Service populating business data on app startup.
 */
@Slf4j
@Component
public class PersonImporter {

	@Autowired
	PersonRepository personRepository;

	@PostConstruct
	public void init() throws URISyntaxException {
		Path p = Paths.get(Application.class.getResource("/input.csv").toURI());

		try (Stream<String> stream = Files.lines(p)) {

			List<Person> persons = stream.map(l -> {
				String[] terms = l.split(",");
				if (terms.length < 3) {
					log.warn("Not enough terms in line (should be at least 3): {}", l);
					return null;
				}
				Person person = new Person();
				person.setName(terms[0]);
				try {
					person.setMonthOfBirth(Integer.valueOf(terms[1].trim()));
					person.setDayOfBirth(Integer.valueOf(terms[2].trim()));
				} catch (NumberFormatException e) {
					log.warn("Wrong number format on line: {}" + l);
					return null;
				}
				return person;
			}).collect(Collectors.toList());

			personRepository.save(persons);

		} catch (IOException e) {
			log.error("Error reading input data file", e);
		}
	}
}
