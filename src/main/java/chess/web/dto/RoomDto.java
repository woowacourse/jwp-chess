package chess.web.dto;

import chess.domain.room.RoomName;

public class RoomDto {

    private final int RoomNumber;
    private final RoomName RoomName;

    private RoomDto(int roomNumber, RoomName roomName) {
        RoomNumber = roomNumber;
        RoomName = roomName;
    }

    public static RoomDto of(int number, RoomName name) {
        return new RoomDto(number, name);
    }

    public int getRoomNumber() {
        return RoomNumber;
    }

    public RoomName getRoomName() {
        return RoomName;
    }
}
