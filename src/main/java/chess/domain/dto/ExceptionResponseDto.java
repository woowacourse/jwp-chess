package chess.domain.dto;

public class ExceptionResponseDto {

    private final String errorMessage;

    public ExceptionResponseDto(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
