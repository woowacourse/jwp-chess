package chess.dto;

public class ErrorMessageDto {
    private final String error;
    private final String status;

    public ErrorMessageDto(String error, String status) {
        this.error = error;
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public String getStatus() {
        return status;
    }
}
