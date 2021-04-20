package chess.dto;

public class CommonResponseBody<T> {
    private final String message;
    private final T item;

    public CommonResponseBody(String message, T item) {
        this.message = message;
        this.item = item;
    }

    public CommonResponseBody(String message) {
        this(message, null);
    }

    public String getMessage() {
        return message;
    }

    public T getItem() {
        return item;
    }
}
