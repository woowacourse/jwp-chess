package chess.exception;

public enum ErrorCode {
    // COMMON
    NOT_FOUND(400, "C001", "[ERROR] 찾을 수 없는 정보입니다."),

    // CHESS PIECE
    INVALID_PIECE_MOVE(400, "P001", "[ERROR] 체스 기물을 해당 위치로 이동할 수 없습니다."),

    // CHESS ROOM
    INVALID_ROOM_COMMAND(400, "R001", "[ERROR] 유효하지 않는 방 명령입니다."),
    DUPLICATE_ROOM(400, "R002", "[ERROR] 중복된 방입니다."),

    // DB
    DB_COMMON(400, "D001", "[ERROR] DB에서 예외가 발생했습니다."),

    // ETC
    ETC(400, "E001", "[ERROR] 기타 예외")
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
