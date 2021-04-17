package chess.domain.response;

public enum StatusEnum {
    OK(200, "OK"),
    BAD_REQUEST(400, "BAD_REQUEST"),
    NOT_FOUND(404, "NOT_FOUND"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR");

    final int statusCode;
    final String info;

    StatusEnum(final int statusCode, final String info) {
        this.statusCode = statusCode;
        this.info = info;
    }
}
