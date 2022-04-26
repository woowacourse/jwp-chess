package chess.dto;

public class ExceptionDto {

    private boolean ok;
    private String message;

    public ExceptionDto() {
    }

    public ExceptionDto(String message) {
        this.ok = false;
        this.message = message;
    }

    public boolean isOk() {
        return ok;
    }

    public String getMessage() {
        return message;
    }
}
