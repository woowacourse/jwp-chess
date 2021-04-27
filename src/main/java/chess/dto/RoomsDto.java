package chess.dto;

import java.util.List;

public class RoomsDto {
    private List<String> roomNames;
    private List<Integer> roomIds;

    public RoomsDto() {
    }

    private RoomsDto(List<String> roomNames, List<Integer> roomIds) {
        this.roomNames = roomNames;
        this.roomIds = roomIds;
    }

    public static RoomsDto of(List<String> roomNames, List<Integer> roomIds) {
        return new RoomsDto(roomNames, roomIds);
    }

    public List<String> getRoomNames() {
        return roomNames;
    }

    public List<Integer> getRoomIds() {
        return roomIds;
    }
}
