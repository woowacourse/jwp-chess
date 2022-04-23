package chess.controller.dto;

public class ChessGameErrorResponse {

	private String message;

	private ChessGameErrorResponse() {
	}

	public ChessGameErrorResponse(String message) {
		this.message = message;
	}

	public static ChessGameErrorResponse from(RuntimeException runtimeException) {
		return new ChessGameErrorResponse(runtimeException.getMessage());
	}

	public String getMessage() {
		return message;
	}
}
