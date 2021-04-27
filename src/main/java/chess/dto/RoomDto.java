package chess.dto;

import java.util.Objects;

public class RoomDto {
    private String roomName;
    private int roomId;

    public RoomDto() {
    }

    private RoomDto(String roomName, int roomId) {
        this.roomName = roomName;
        this.roomId = roomId;
    }

    public static RoomDto of(String roomName, int roomId) {
        return new RoomDto(roomName, roomId);
    }

    public String getRoomName() {
        return roomName;
    }

    public int getRoomId() {
        return roomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomDto roomDto = (RoomDto) o;
        return roomId == roomDto.roomId && roomName.equals(roomDto.roomName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomName, roomId);
    }
}
