package wooteco.chess.support;

public enum ChessResponseCode implements ResponseCode {
    SUCCESS(200, "성공"),
    WIN(201, "승"),
    LOSE(202, "패"),
    BAD_REQUEST(400, "잘못된 요청"),
    CANNOT_FIND_ROOM_ID(401,"유효 하지 않은 방"),
    NOT_YOUR_TURN(402,"차례 아님"),
    HAS_OBSTACLE(403, "장애물 있음"),
    ROOM_IS_FULL(404,"방이 꽉 찼음");

    private Integer code;
    private String message;

    ChessResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String getCode() {
        return null;
    }

    @Override
    public String getMessage() {
        return null;
    }
}
