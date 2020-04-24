package wooteco.chess.view.response;

public class StandardResponse {
	private ResponseStatus status;
	private Object data;

	public StandardResponse(ResponseStatus status) {
		this.status = status;
	}

	public StandardResponse(ResponseStatus status, String data) {
		this.status = status;
		this.data = data;
	}

	public StandardResponse(ResponseStatus status, Object data) {
		this.status = status;
		this.data = data;
	}

	public ResponseStatus getStatus() {
		return status;
	}

	public Object getData() {
		return data;
	}
}
