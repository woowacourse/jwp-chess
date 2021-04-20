package chess.service.dto;

public class ErrorResponseDto {

    private String message;

    public ErrorResponseDto() {}

    public ErrorResponseDto(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
