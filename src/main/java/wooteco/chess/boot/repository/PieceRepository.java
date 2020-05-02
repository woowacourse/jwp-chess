package wooteco.chess.boot.repository;

import org.springframework.data.repository.CrudRepository;
import wooteco.chess.boot.entity.PieceEntity;

import java.util.List;

public interface PieceRepository extends CrudRepository<PieceEntity, Long> {

    @Override
    List<PieceEntity> findAll();
}
