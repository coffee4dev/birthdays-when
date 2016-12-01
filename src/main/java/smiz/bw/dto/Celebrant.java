package smiz.bw.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by smiz on 30/11/16.
 */
@Data
@AllArgsConstructor
public class Celebrant {

	private String name;

	@JsonProperty("days_left")
	private int daysLeft;

}
