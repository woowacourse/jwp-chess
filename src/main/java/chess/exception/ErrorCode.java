package chess.exception;

public enum ErrorCode {
    INVALID_MOVE(400, "잘못된 움직임 요청입니다."),
    INVALID_PIECE_SELECT(400, "잘못된 말 선택입니다."),
    INVALID_POSITION(400, "정의되서는 안되는 포지션 값 입니다."),
    CANNOT_KILL_SAME_COLOR(400, "같은 색깔의 말을 잡을 수 없습니다."),
    FINISHED_GAME(400, "이미 종료된 게임입니다."),
    NO_ROOM_TO_LOAD(400, "해당 Id에 방 번호가 없습니다."),
    NO_ROOM_TO_DELETE(400, "해당 Id에 방 번호가 없습니다.");

    private int statusCode;
    private String message;

    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
