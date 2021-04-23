package chess.domain.dto;

public class ErrorMessageDto {

    private final String errorMessage;

    public ErrorMessageDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
