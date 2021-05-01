package chess.controller.room;

import chess.chessgame.domain.room.Room;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

class ActiveRoomsResponseDto {
    private final Map<Long, RoomResponseDto> activeRooms;

    public ActiveRoomsResponseDto(List<Room> rooms) {
        this(rooms.stream()
                .collect(toMap(Room::getRoomId, room -> new RoomResponseDto(room.isMaxUser(), room.getRoomName()))));
    }

    public ActiveRoomsResponseDto(Map<Long, RoomResponseDto> activeRooms) {
        this.activeRooms = activeRooms;
    }

    public Map<Long, RoomResponseDto> getActiveRooms() {
        return activeRooms;
    }
}
