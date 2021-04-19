package chess.service.dto;

public class CommonResponseDto<T> {

    private T body;
    private int statusCode;
    private String message;

    public CommonResponseDto(final int statusCode) {
        this(null, statusCode, "");
    }

    public CommonResponseDto(final int statusCode, final String message) {
        this(null, statusCode, message);
    }

    public CommonResponseDto(final T body, final int statusCode) {
        this(body, statusCode, "");
    }

    public CommonResponseDto(final T body, final int statusCode, final String message) {
        this.body = body;
        this.statusCode = statusCode;
        this.message = message;
    }

    public T getBody() {
        return body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}