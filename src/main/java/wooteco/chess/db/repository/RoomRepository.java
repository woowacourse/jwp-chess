package wooteco.chess.db.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import wooteco.chess.db.entity.RoomEntity;

@Repository
public interface RoomRepository extends CrudRepository<RoomEntity, Long> {

	@Modifying
	@Query("update room r set r.turn = :turnId where r.room_id = :roomId")
	void updateTurn(@Param("roomId") Long roomId, @Param("turnId") Long turnId);
}
