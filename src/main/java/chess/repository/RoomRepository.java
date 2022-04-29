package chess.repository;

import chess.domain.Room;

import java.util.List;
import java.util.Optional;

public interface RoomRepository {
    int save(Room room);

    Optional<Room> findByName(String name);

    Optional<Room> findById(int roomId);

    void deleteById(int id);

    List<Room> findAll();
}
