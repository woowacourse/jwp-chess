package wooteco.chess.db.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import wooteco.chess.db.entity.BoardEntity;

@Repository
public interface BoardRepository extends CrudRepository<BoardEntity, Long> {
	@Query("select * from piece where room_id =:roomId")
	List<BoardEntity> findByRoomId(Long roomId);

	@Modifying
	@Query("update piece set piece_name = :name , piece_team = :team where room_id = :roomId and piece_position = :position")
	void updatePiece(long roomId, String position, String team, String name);
}
