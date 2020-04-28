package chess.repository;

import chess.dto.PieceDto;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PieceRepository extends CrudRepository<PieceDto, Integer> {

	@Query("SELECT * FROM piece WHERE room_id=:room_id")
	List<PieceDto> findPiecesByRoomId(@Param("room_id") final int roomId);

	@Modifying
	@Query("DELETE FROM piece WHERE room_id = :room_id")
	void deletePieces(@Param("room_id") final int roomId);
}
