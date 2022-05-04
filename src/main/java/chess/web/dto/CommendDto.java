package chess.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommendDto {

	@JsonProperty("source")
	private final String source;
	@JsonProperty("target")
	private final String target;

	public CommendDto(
		@JsonProperty("source") String source,
		@JsonProperty("target") String target) {
		this.source = source;
		this.target = target;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}
}
