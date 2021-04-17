package chess.exception;

public class NoLogsException extends RuntimeException {
    private final String roomId;

    public NoLogsException(String roomId) {
        super("이어할 게임이 없습니다. 새로운게임을 눌러주세요!");
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }
}
