package wooteco.chess.view.dto.requestdto;

public class PositionRequestDto {
	private String from;
	private String to;

	public PositionRequestDto() {
	}

	public PositionRequestDto(String from, String to) {
		this.from = from;
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public String getTo() {
		return to;
	}

	@Override
	public String toString() {
		return "PositionRequestDTO{" +
			"from='" + from + '\'' +
			", to='" + to + '\'' +
			'}';
	}
}
