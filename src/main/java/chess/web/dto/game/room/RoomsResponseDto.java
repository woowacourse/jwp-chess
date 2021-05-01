package chess.web.dto.game.room;

import java.util.List;

public class RoomsResponseDto {

    private final List<RoomResponseDto> roomResponseDtos;

    private RoomsResponseDto(final List<RoomResponseDto> roomResponseDtos) {
        this.roomResponseDtos = roomResponseDtos;
    }

    public static RoomsResponseDto from(final List<RoomResponseDto> roomResponseDtos) {
        return new RoomsResponseDto(roomResponseDtos);
    }

    public List<RoomResponseDto> getRoomResponseDtos() {
        return roomResponseDtos;
    }

}
