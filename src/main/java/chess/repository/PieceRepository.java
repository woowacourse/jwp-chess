package chess.repository;

import chess.domain.pieces.Piece;
import chess.dto.PieceDto;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.parser.Entity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public interface PieceRepository extends CrudRepository<PieceDto, Integer> {

	@Modifying
	@Query("INSERT INTO piece(piece_type, team, coordinate, room_id) "
			+ "VALUES(:piece_type, :team, :coordinate, :room_id)")
	void save(@Param("piece_type") final String pieceType, @Param("team") final String team,
			  @Param("coordinate") final String coordinate, @Param("room_id") final int roomId);

	@Query("SELECT * FROM piece WHERE room_id=:room_id")
	List<PieceDto> findPiecesByRoomId(@Param("room_id") final int roomId);

	@Modifying
	@Query("DELETE FROM piece WHERE room_id = :room_id")
	void deletePieces(@Param("room_id") final int roomId);
}
