package chess.controller;

import java.util.Map;

public class RoomListDTO {
    private final Map<String, String> roomList;

    public RoomListDTO(Map<String, String> roomList) {
        this.roomList = roomList;
    }

    public Map<String, String> getRoomList() {
        return roomList;
    }
}
