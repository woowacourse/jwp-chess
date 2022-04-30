package chess.controller.dto.response;

public class ChessGameStatusResponse {

	private boolean end;

	private ChessGameStatusResponse() {
	}

	public ChessGameStatusResponse(boolean isEnd) {
		this.end = isEnd;
	}

	public boolean isEnd() {
		return end;
	}
}
