package chess.dto;

import java.util.Map;

public class RoomListDto {
    private final Map<String, String> roomList;

    public RoomListDto(Map<String, String> roomList) {
        this.roomList = roomList;
    }

    public Map<String, String> getRoomList() {
        return roomList;
    }
}
