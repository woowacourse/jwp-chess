package chess.dto.response;

import java.util.List;

public class RoomsResponseDto {

    private List<Integer> roomIds;

    public RoomsResponseDto(List<Integer> roomIds) {
        this.roomIds = roomIds;
    }

    public List<Integer> getRoomIds() {
        return roomIds;
    }
}
