package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wooteco.chess.entity.Room;

import java.util.Optional;

@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {
    @Query("SELECT * FROM room WHERE name = :name")
    Optional<Room> findByName(@Param("name") String name);
}
