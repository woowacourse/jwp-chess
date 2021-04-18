package chess.dto.response;

import org.springframework.http.HttpStatus;

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

    public Response(HttpStatus httpStatus) {
        this(httpStatus.value(), httpStatus.getReasonPhrase(), null);
    }

    public Response(HttpStatus httpStatus, T data) {
        this(httpStatus.value(), httpStatus.getReasonPhrase(), data);
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
