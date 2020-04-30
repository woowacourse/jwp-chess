package wooteco.chess.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wooteco.chess.entity.Room;

public interface RoomRepository extends CrudRepository<Room, Long> {
    @Query("SELECT * FROM room WHERE room.name = :name")
    Optional<Room> findByName(@Param("name") String name);
}
