package wooteco.chess.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wooteco.chess.domain.entity.ChessGameEntity;

public interface ChessGameRepository extends CrudRepository<ChessGameEntity, Long> {
	@Query("select * from chessGame where room_id = :roomId")
	Optional<ChessGameEntity> findByRoomId(@Param("roomId") String gameId);
}
