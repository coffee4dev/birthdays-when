package smiz.bw.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * POJO containing result of birthdays calculation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

	public static final String STATUS_DONE = "done";
	public static final String STATUS_PENDING = "pending";

	private Instant createdAt = Instant.now();

	private String status = STATUS_PENDING;
	private List<Celebrant> persons = Collections.emptyList();

	@JsonIgnore
	public boolean isDone() {
		return STATUS_DONE.equals(status);
	}

	@JsonIgnore
	public Instant getCreatedAt() {
		return this.createdAt;
	}
}
