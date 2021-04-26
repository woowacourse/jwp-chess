package chess.dto.response;

import java.util.List;

public class RoomsResponseDto {

    private final List<String> roomNames;

    public RoomsResponseDto(List<String> roomNames) {
        this.roomNames = roomNames;
    }

    public List<String> getRoomNames() {
        return roomNames;
    }
}
