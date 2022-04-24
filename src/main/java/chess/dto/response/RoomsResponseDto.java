package chess.dto.response;

import chess.entity.RoomEntity;
import java.util.List;
import java.util.stream.Collectors;

public class RoomsResponseDto {

    private List<RoomResponseDto> roomResponseDtos;

    public RoomsResponseDto(final List<RoomResponseDto> roomResponseDtos) {
        this.roomResponseDtos = roomResponseDtos;
    }

    public static RoomsResponseDto of(final List<RoomEntity> rooms) {
        return new RoomsResponseDto(rooms.stream()
            .map(RoomResponseDto::of)
            .collect(Collectors.toList()));
    }

    public List<RoomResponseDto> getRoomResponseDtos() {
        return roomResponseDtos;
    }
}
