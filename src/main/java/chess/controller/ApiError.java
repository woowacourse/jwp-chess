package chess.controller;

import chess.exception.ErrorCode;

public class ApiError {

    private final String message;
    private final String detailMessage;
    private final int status;
    private final String code;

    private ApiError(ErrorCode errorCode, String detailMessage) {
        this(errorCode.getMessage(), detailMessage, errorCode.getStatus(), errorCode.getCode());
    }

    private ApiError(String message, String detailMessage, int status, String code) {
        this.message = message;
        this.detailMessage = detailMessage;
        this.status = status;
        this.code = code;
    }

    public static ApiError of(ErrorCode errorCode, String detailMessage) {
        return new ApiError(errorCode, detailMessage);
    }

    public String getMessage() {
        return message;
    }

    public String getDetailMessage() {
        return detailMessage;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }
}
