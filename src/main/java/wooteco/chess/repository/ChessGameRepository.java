package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wooteco.chess.entity.ChessGame;

import java.util.Optional;

public interface ChessGameRepository extends CrudRepository<ChessGame, Long> {
	@Query("SELECT * FROM chess_game WHERE room_no = :room_no")
	Optional<ChessGame> findChessGameByRoomNo(@Param("room_no") int roomNo);
}
