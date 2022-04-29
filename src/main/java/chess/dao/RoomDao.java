package chess.dao;

import java.util.List;
import java.util.Optional;

import chess.entity.Room;

public interface RoomDao {
    void save(Room room);

    Optional<Room> findByName(String name);

    Optional<Room> findByNameAndPassword(String name, String password);

    Optional<Room> findById(long roomId);

    void update(long id, String turn);

    List<Room> findAll();

    Optional<Room> findByIdAndPassword(long id, String password);

    void delete(long roomId);
}
