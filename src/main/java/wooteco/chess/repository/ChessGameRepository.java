package wooteco.chess.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wooteco.chess.domain.entity.ChessGame;

public interface ChessGameRepository extends CrudRepository<ChessGame, Long> {
	@Override
	List<ChessGame> findAll();

	@Query("select * from chess_game where room_id = :roomId")
	Optional<ChessGame> findByRoomId(@Param("roomId") String roomId);
}
