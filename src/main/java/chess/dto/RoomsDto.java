package chess.dto;

import java.util.List;

public class RoomsDto {
    private final List<RoomDto> rooms;

    public RoomsDto(List<RoomDto> rooms) {
        this.rooms = List.copyOf(rooms);
    }

    public List<RoomDto> getRooms() {
        return rooms;
    }
}
