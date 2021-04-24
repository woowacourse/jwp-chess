package chess.exception;

public class NotEnoughPlayerException extends RoomException {
    public NotEnoughPlayerException(final String roomId) {
        super(roomId, "참여인원이 부족합니다.");
    }
}
