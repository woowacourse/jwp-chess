package chess.dto;

import java.util.List;

public class RoomsDto {
    private List<String> roomNames;
    private List<Integer> roomNumbers;

    public RoomsDto() {
    }

    private RoomsDto(List<String> roomNames, List<Integer> roomNumbers) {
        this.roomNames = roomNames;
        this.roomNumbers = roomNumbers;
    }

    public static RoomsDto of(List<String> roomNames, List<Integer> roomNumbers) {
        return new RoomsDto(roomNames, roomNumbers);
    }

    public List<String> getRoomNames() {
        return roomNames;
    }

    public List<Integer> getRoomNumbers() {
        return roomNumbers;
    }
}
