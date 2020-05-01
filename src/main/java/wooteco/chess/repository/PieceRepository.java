package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;
import wooteco.chess.repository.entity.PieceEntity;

import java.util.UUID;

public interface PieceRepository extends CrudRepository<PieceEntity, Long> {
    
}
