package chess.controller.dto;

import chess.domain.Position;

public class PieceMoveRequest {

	private String source;
	private String target;

	private PieceMoveRequest() {
	}

	public PieceMoveRequest(String source, String target) {
		this.source = source;
		this.target = target;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}

	public Position toSourcePosition() {
		return Position.from(source);
	}

	public Position toTargetPosition() {
		return Position.from(target);
	}
}
