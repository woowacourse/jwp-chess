package chess.controller.web.dto;

import chess.chessgame.domain.room.Room;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class ActiveRoomsResponseDto {
    private final Map<Long, String> activeRooms;

    public ActiveRoomsResponseDto(List<Room> rooms) {
        this(rooms.stream()
                .collect(toMap(Room::getRoomId, Room::getRoomName)));
    }

    public ActiveRoomsResponseDto(Map<Long, String> activeRooms) {
        this.activeRooms = activeRooms;
    }

    public Map<Long, String> getActiveRooms() {
        return activeRooms;
    }
}
