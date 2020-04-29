package spring.entity.repository;

import org.springframework.data.repository.CrudRepository;
import spring.entity.PieceEntity;

public interface PieceRepository extends CrudRepository<PieceEntity, Long> {
}
