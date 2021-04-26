package chess.exception.room;

import org.springframework.http.HttpStatus;

public class NotExistRoomException extends RoomException {

    public NotExistRoomException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "방이 존재하지 않습니다.");
    }
}
