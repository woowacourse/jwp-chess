package chess.service.dto;

public enum ResponseCode {
    OK(200, "응답 성공"),
    BAD_REQUEST(400, "응답 실패");

    private final int code;
    private final String message;

    ResponseCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    public int code() {
        return code;
    }

    public String message() {
        return message;
    }
}
