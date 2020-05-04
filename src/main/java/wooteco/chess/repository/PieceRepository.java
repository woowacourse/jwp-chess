package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wooteco.chess.entity.PieceEntity;

public interface PieceRepository extends CrudRepository<PieceEntity, Long> {
	@Modifying
	@Query("UPDATE piece SET name = :name WHERE position = :position AND room_entity = :roomId")
	void update(@Param("name") String name, @Param("position") String position, @Param("roomId") Long roomId);
}
