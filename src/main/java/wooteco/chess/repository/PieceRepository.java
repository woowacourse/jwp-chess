package wooteco.chess.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wooteco.chess.domain.entity.PieceEntity;

public interface PieceRepository extends CrudRepository<PieceEntity, Long> {
	@Query("select * from piece where room_id = :room_id")
	List<PieceEntity> findAllByRoomId(@Param("room_id") String roomId);
}
