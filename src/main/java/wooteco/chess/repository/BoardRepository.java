package wooteco.chess.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import wooteco.chess.entity.BoardEntity;

@Repository
public interface BoardRepository extends CrudRepository<BoardEntity, Long> {
	@Override
	List<BoardEntity> findAll();
}
