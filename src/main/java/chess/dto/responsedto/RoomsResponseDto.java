package chess.dto.responsedto;

import chess.dto.RoomDto;

import java.util.List;

public class RoomsResponseDto implements ResponseDto {
    private final List<RoomDto> rooms;

    public RoomsResponseDto(List<RoomDto> rooms) {
        this.rooms = rooms;
    }

    public List<RoomDto> getRooms() {
        return rooms;
    }
}
