package smiz.bw.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * Created by smiz on 01/12/16.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

	public static final String STATUS_DONE = "done";
	public static final String STATUS_PENDING = "pending";

	private String status = STATUS_PENDING;
	private List<Celebrant> persons = Collections.EMPTY_LIST;

	@JsonIgnore
	public boolean isDone() {
		return STATUS_DONE.equals(status);
	}
}
