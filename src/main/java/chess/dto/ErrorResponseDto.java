package chess.dto;

import chess.controller.StatusCode;

public class ErrorResponseDto {

    private final String errorMessage;
    private final StatusCode statusCode;

    private ErrorResponseDto(final String errorMessage, final StatusCode statusCode) {
        this.errorMessage = errorMessage;
        this.statusCode = statusCode;
    }

    public static ErrorResponseDto of(final Exception e, final StatusCode statusCode) {
        return new ErrorResponseDto(e.getMessage(), statusCode);
    }
}
