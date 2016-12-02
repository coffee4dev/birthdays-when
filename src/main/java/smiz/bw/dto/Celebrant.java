package smiz.bw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import smiz.bw.model.Person;

/**
 * Created by smiz on 30/11/16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Celebrant {

	private String name;

	@JsonProperty("days_left")
	private long daysLeft;

}
