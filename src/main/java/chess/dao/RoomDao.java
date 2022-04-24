package chess.dao;

import java.util.Optional;

import chess.entity.Room;

public interface RoomDao {
    void save(Room room);

    Optional<Room> findByName(String name);

    void update(long id, String turn);
}
