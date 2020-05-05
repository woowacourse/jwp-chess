package wooteco.chess.dto;

import wooteco.chess.domain.piece.Position;

public class MoveRequestDto {
	private Position source;
	private Position target;

	public MoveRequestDto() {

	}

	public Position getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = Position.from(source);
	}

	public Position getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = Position.from(target);
	}
}
