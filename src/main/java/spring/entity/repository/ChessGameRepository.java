package spring.entity.repository;

import org.springframework.data.repository.CrudRepository;
import spring.entity.ChessGameEntity;

import java.util.List;

public interface ChessGameRepository extends CrudRepository<ChessGameEntity, Long> {
    @Override
    List<ChessGameEntity> findAll();
}
