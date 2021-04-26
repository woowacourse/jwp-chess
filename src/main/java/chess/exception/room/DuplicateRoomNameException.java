package chess.exception.room;

import org.springframework.http.HttpStatus;

public class DuplicateRoomNameException extends RoomException {

    public DuplicateRoomNameException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "해당 이름의 방이 이미 존재합니다.");
    }
}
