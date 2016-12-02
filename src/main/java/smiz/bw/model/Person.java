package smiz.bw.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by smiz on 01/12/16.
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Person {

	@Id
	@GeneratedValue(strategy= GenerationType.AUTO)
	private long id;

	private String name;
	private int monthOfBirth;
	private int dayOfBirth;

}
