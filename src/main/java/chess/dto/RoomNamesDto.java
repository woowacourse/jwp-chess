package chess.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoomNamesDto {
    private final List<String> roomNames;

    public RoomNamesDto(List<String> roomNames) {
        this.roomNames = new ArrayList<>(roomNames);
    }

    public List<String> getRoomNames() {
        return Collections.unmodifiableList(roomNames);
    }
}
