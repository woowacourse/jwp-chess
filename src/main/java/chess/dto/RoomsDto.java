package chess.dto;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomsDto roomsDto = (RoomsDto) o;
        return roomDtos.equals(roomsDto.roomDtos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomDtos);
    }
}
