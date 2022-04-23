package chess.controller.dto.response;

public class ChessGameStatusResponse {

	private boolean isEnd;

	private ChessGameStatusResponse() {
	}

	public ChessGameStatusResponse(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public boolean isEnd() {
		return isEnd;
	}
}
