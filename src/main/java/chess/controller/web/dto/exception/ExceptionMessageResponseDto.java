package chess.controller.web.dto.exception;

public class ExceptionMessageResponseDto {

    private String exceptionMessage;

    public ExceptionMessageResponseDto() {
    }

    public ExceptionMessageResponseDto(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public void setExceptionMessage(String exceptionMessage) {
        this.exceptionMessage = exceptionMessage;
    }

    public String getExceptionMessage() {
        return exceptionMessage;
    }
}
