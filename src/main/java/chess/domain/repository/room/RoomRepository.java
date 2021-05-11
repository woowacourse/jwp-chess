package chess.domain.repository.room;

import chess.domain.room.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    Long save(Room room);

    void update(Room room);

    Optional<Room> findById(Long id);

    List<Room> findByPlayingGame();

    Room findByGameId(Long gameId);
}
