package wooteco.chess.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import wooteco.chess.entity.PieceEntity;

public interface PieceRepository extends CrudRepository<PieceEntity, Long> {
	@Override
	List<PieceEntity> findAll();

	@Modifying
	@Query("UPDATE piece SET name = :name WHERE position = :position AND board_entity = :boardId")
	void update(@Param("name") String name, @Param("position") String position, @Param("boardId") Long boardId);
}
