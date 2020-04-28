package wooteco.chess.dao.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import wooteco.chess.dto.RoomDto2;

@Repository
public interface RoomRepository extends CrudRepository<RoomDto2, Long> {
	
	@Modifying
	@Query("update room r set r.turn = :turnId where r.room_id = :roomId")
	void updateTurn(@Param("roomId") Long roomId, @Param("turnId") Long turnId);
}
