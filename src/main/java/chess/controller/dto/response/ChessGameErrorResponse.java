package chess.controller.dto.response;

public class ChessGameErrorResponse {

	private String message;

	private ChessGameErrorResponse() {
	}

	private ChessGameErrorResponse(String message) {
		this.message = message;
	}

	public static ChessGameErrorResponse from(RuntimeException runtimeException) {
		return new ChessGameErrorResponse(runtimeException.getMessage());
	}

	public static ChessGameErrorResponse create(String message) {
		return new ChessGameErrorResponse(message);
	}

	public String getMessage() {
		return message;
	}
}
