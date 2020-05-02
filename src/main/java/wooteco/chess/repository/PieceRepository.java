package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;

import wooteco.chess.repository.entity.PieceEntity;

public interface PieceRepository extends CrudRepository<PieceEntity, Long> {

}
