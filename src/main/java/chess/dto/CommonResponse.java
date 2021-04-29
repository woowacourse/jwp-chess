package chess.dto;

public class CommonResponse<T> {
    private final String message;
    private final T item;

    public CommonResponse(String message, T item) {
        this.message = message;
        this.item = item;
    }

    public String getMessage() {
        return message;
    }

    public T getItem() {
        return item;
    }
}
