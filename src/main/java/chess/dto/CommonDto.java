package chess.dto;

public class CommonDto<T> {
    private final String message;
    private final T item;

    public CommonDto(String message, T item) {
        this.message = message;
        this.item = item;
    }

    public CommonDto(String message) {
        this(message, null);
    }

    public String getMessage() {
        return message;
    }

    public T getItem() {
        return item;
    }
}
