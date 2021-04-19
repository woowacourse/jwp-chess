package chess.domain.dto;

import java.util.List;

public class RoomListDto {
    private List<String> roomList;

    public RoomListDto() {}

    public RoomListDto(List<String> roomList) {
        this.roomList = roomList;
    }

    public List<String> getRoomList() {
        return roomList;
    }
}
