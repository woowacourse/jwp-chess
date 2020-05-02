package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wooteco.chess.entity.Move;

import java.util.List;
import java.util.Optional;

@Repository
public interface MoveRepository extends CrudRepository<Move, Long> {
    @Query("SELECT * FROM move WHERE room_id = :roomId ORDER BY id ASC")
    Optional<List<Move>> findByRoomId(@Param("roomId") Long roomId);
}
