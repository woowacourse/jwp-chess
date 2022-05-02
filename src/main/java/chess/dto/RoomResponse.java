package chess.dto;

import chess.domain.Room;

public class RoomResponse {

    private final Long id;
    private final String roomName;

    public RoomResponse() {
        this(null, null);
    }

    public RoomResponse(Long id, String roomName) {
        this.id = id;
        this.roomName = roomName;
    }

    public static RoomResponse from(Room room) {
        return new RoomResponse(room.getId(), room.getRoomName());
    }

    public Long getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }
}
