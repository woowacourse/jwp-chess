package chess.dto;

public class ErrorRes {

    private String message;

    public ErrorRes() {
    }

    public ErrorRes(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
