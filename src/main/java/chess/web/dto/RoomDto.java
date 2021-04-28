package chess.web.dto;

import chess.domain.room.Room;

public class RoomDto {
    private final Long roomId;
    private final String roomName;

    public RoomDto(Room room) {
        this.roomId = room.getRoomId();
        this.roomName = room.getRoomName();
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getRoomName() {
        return roomName;
    }

}
