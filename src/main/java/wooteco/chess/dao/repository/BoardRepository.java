package wooteco.chess.dao.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import wooteco.chess.dto.BoardDto;

@Repository
public interface BoardRepository extends CrudRepository<BoardDto, Long> {
	@Query("select * from piece where room_id =:roomId")
	List<BoardDto> findByRoomId(Long roomId);

	@Modifying
	@Query("update piece set piece_name = :name , piece_team = :team where room_id = :roomId and piece_position = :position")
	void updatePiece(long roomId, String position, String team, String name);
}
