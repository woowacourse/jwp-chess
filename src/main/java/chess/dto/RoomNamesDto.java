package chess.dto;

import java.util.*;

public class RoomNamesDto {
    private final Map<Integer, String> rooms;

    public RoomNamesDto(Map<Integer, String> rooms) {
        this.rooms = new HashMap<>(rooms);
    }

    public Map<Integer, String> getRoomNames() {
        return Collections.unmodifiableMap(rooms);
    }
}
