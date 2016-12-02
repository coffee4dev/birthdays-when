package smiz.bw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import smiz.bw.model.Person;

/**
 * POJO containing person name and number of days left until his/her birthday.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Celebrant {

	private String name;

	@JsonProperty("days_left")
	private long daysLeft;

}
