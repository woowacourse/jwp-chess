package chess.controller.dto.request;

import chess.domain.Position;
import javax.validation.constraints.NotEmpty;

public class PieceMoveRequest {

	@NotEmpty(message = "초기 위치 값을 입력해 주세요.")
	private String source;

	@NotEmpty(message = "도착 위치 값을 입력해 주세요.")
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
