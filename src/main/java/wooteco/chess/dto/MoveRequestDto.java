package wooteco.chess.dto;

import wooteco.chess.domain.piece.Position;

public class MoveRequestDto {
	private String source;
	private String target;

	public MoveRequestDto(String source, String target) {
		this.source = source;
		this.target = target;
	}

	public String getFrom() {
		return source;
	}

	public void setFrom(String from) {
		this.source = from;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Position source() {
		return Position.from(source);
	}

	public Position target() {
		return Position.from(target);
	}
}
