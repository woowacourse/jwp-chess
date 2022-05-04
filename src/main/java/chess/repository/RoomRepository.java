package chess.repository;

import chess.domain.room.Room;
import chess.dto.response.RoomPageDto;

public interface RoomRepository {
    Room get(int roomId);

    RoomPageDto getAll(int page, int size);

    int add(Room room);

    void update(int roomId, Room room);

    void remove(int roomId);
}
