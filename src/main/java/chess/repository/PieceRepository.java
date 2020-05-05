package chess.repository;

import chess.entity.PieceEntity;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PieceRepository extends CrudRepository<PieceEntity, Integer> {

	@Query("SELECT * FROM piece WHERE room_id=:room_id")
	List<PieceEntity> findPiecesByRoomId(@Param("room_id") final int roomId);

	@Modifying
	@Query("DELETE FROM piece WHERE room_id = :room_id")
	void deletePieces(@Param("room_id") final int roomId);
}
