package wooteco.chess.view.response;

public class ResponseDto {
	private ResponseStatus status;
	private Object data;

	public ResponseDto(ResponseStatus status) {
		this.status = status;
	}

	public ResponseDto(ResponseStatus status, String data) {
		this.status = status;
		this.data = data;
	}

	public ResponseDto(ResponseStatus status, Object data) {
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
