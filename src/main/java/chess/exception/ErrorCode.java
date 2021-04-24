package chess.exception;

public enum ErrorCode {
    // CHESS
    INVALID_PIECE_MOVE(400, "C001", "[ERROR] 체스 기물을 해당 위치로 이동할 수 없습니다."),
    INVALID_ROOM_COMMAND(400, "C002", "[ERROR] 유효하지 않는 방 명령입니다.")
    ;

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
