package chess.dto;

import java.util.List;

public class RoomsDto {

    private List<RoomDto> rooms;

    public RoomsDto(List<RoomDto> rooms) {
        this.rooms = rooms;
    }


    public List<RoomDto> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomDto> rooms) {
        this.rooms = rooms;
    }
}
