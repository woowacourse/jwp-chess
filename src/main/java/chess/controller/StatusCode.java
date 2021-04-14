package chess.controller;

public enum StatusCode {
    SUCCESSFUL(200),
    INTERNAL_SERVER_ERROR(500),
    BAD_GATEWAY(500),
    CONFLICT(409),
    BAD_REQUEST(400);

    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
