package wooteco.chess.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import wooteco.chess.entity.PieceEntity;

public interface PieceRepository extends CrudRepository<PieceEntity, Long> {
	@Override
	List<PieceEntity> findAll();
}
