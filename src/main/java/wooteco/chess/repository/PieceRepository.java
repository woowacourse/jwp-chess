package wooteco.chess.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wooteco.chess.domain.entity.PieceEntity;

public interface PieceRepository extends CrudRepository<PieceEntity, Long> {
	@Query("select * from piece where board = :board")
	List<PieceEntity> findAllByBoardId(@Param("board") String board);

	@Query("select * from piece where chess_game = :chessGame and position = :position")
	Optional<PieceEntity> findByGameIdAndPosition(@Param("chessGame") Long chessGame, @Param("position") String position);
}
