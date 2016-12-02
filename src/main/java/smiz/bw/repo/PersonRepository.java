package smiz.bw.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import smiz.bw.model.Person;

import java.util.stream.Stream;

/**
 * Created by smiz on 01/12/16.
 */
@Repository
public interface PersonRepository extends CrudRepository<Person, Long> {

	Stream<Person> findByMonthOfBirth(Integer monthOfBirth);
}
