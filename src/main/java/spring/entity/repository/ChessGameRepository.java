package spring.entity.repository;

import org.springframework.data.repository.CrudRepository;
import spring.entity.ChessGameEntity;

public interface ChessGameRepository extends CrudRepository<ChessGameEntity, Long> {
}
