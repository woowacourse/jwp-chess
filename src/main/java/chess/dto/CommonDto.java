package chess.dto;

public class CommonDto<T> {
    private final int statusCode;
    private final String message;
    private final T item;

    public CommonDto(int statusCode, String message, T item) {
        this.statusCode = statusCode;
        this.message = message;
        this.item = item;
    }

    public CommonDto(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
        this.item = null;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public T getItem() {
        return item;
    }
}
