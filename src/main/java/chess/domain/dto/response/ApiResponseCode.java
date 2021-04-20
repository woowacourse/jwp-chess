package chess.domain.dto.response;

public enum ApiResponseCode {
    OK("요청이 성공하였습니다.", 200),
    BAD_REQUEST("잘못된 요청.", 400),
    NOT_FOUND("리소스를 찾지 못했습니다.", 404);

    private final String message;
    private final int httpStatus;

    ApiResponseCode(String message, int httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getMessage() {
        return message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }
}