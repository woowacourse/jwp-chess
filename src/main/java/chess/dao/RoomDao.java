package chess.dao;

import java.util.Optional;

import chess.entity.Room;

public interface RoomDao {
    void save(Room room);

    Optional<Room> findByName(String name);

    Optional<Room> findByNameAndPassword(String name, String password);

    Optional<Room> findById(Long roomId);

    void update(Long id, String turn);
}
