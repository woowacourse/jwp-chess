package wooteco.chess.boot.repository;

import org.springframework.data.repository.CrudRepository;
import wooteco.chess.boot.entity.PieceEntity;

public interface PieceRepository extends CrudRepository<PieceEntity, Long> {

}
