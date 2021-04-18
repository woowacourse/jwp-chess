package chess.dto.response;

public class Response<T> {
    private final int code;
    private final String message;
    private final T data;

    public Response(ResponseCode responseCode) {
        this(responseCode, null);
    }

    public Response(ResponseCode responseCode, T data) {
        this(responseCode.getCode(), responseCode.getMessage(), data);
    }

    public Response(int code, String message) {
        this(code, message, null);
    }

    public Response(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
