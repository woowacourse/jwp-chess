package chess.repository;

import chess.domain.room.Room;
import chess.dto.response.RoomPageDto;
import chess.dto.response.RoomResponseDto;
import java.util.List;

public interface RoomRepository {
    Room get(int roomId);

    List<RoomResponseDto> getAll();

    RoomPageDto getAll(int page, int size);

    int add(Room room);

    void update(int roomId, Room room);

    void remove(int roomId);
}
