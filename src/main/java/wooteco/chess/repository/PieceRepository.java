package wooteco.chess.repository;

import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wooteco.chess.domain.entity.GamePiece;

public interface PieceRepository extends CrudRepository<GamePiece, Long> {
	@Query("SELECT * FROM PIECE WHERE game_id = :gameId AND position = :originalPosition")
	Optional<GamePiece> findByGameIdAndPosition(@Param("gameId") Long gameId,
		@Param("originalPosition") String originalPosition);

	@Query("DELETE FROM PIECE WHERE game_id = :gameId AND position = :targetPosition")
	void deleteByGameIdAndPosition(@Param("gameId") Long gameId, @Param("targetPosition") String targetPosition);
}