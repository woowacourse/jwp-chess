package wooteco.chess.dao.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import wooteco.chess.dto.BoardDto;

@Repository
public interface BoardRepository extends CrudRepository<BoardDto, Long> {
	@Query("select * from piece where room_id =:roomId")
	List<BoardDto> findByRoomId(Long roomId);
}
