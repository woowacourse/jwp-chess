package chess.dto;

import java.util.List;

public class RoomsDto {
    private List<RoomDto> roomDtos;

    public RoomsDto() {
    }

    private RoomsDto(List<RoomDto> roomDtos) {
        this.roomDtos = roomDtos;
    }

    public static RoomsDto of(List<RoomDto> roomDtos) {
        return new RoomsDto(roomDtos);
    }

    public List<RoomDto> getRoomDtos() {
        return roomDtos;
    }
}
