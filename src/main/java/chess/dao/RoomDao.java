package chess.dao;

import java.util.List;
import java.util.Optional;

import chess.dto.RoomDto;
import chess.entity.Room;

public interface RoomDao {
    void save(Room room);

    Optional<Room> findByName(String name);

    Optional<Room> findByNameAndPassword(String name, String password);

    Optional<Room> findById(Long roomId);

    void update(Long id, String turn);

    List<RoomDto> findAll();

    Optional<Room> findByIdAndPassword(Long id, String password);

    void delete(Long roomId);
}
