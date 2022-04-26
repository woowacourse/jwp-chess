package chess.web.dto;

import java.util.List;

public class ReadRoomResultDto {
    private List<RoomDto> rooms;

    public ReadRoomResultDto(List<RoomDto> rooms) {
        this.rooms = rooms;
    }

    public List<RoomDto> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomDto> rooms) {
        this.rooms = rooms;
    }
}
