package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;
import wooteco.chess.repository.entity.GameEntity;

import java.util.UUID;

public interface GameRepository extends CrudRepository<GameEntity, UUID> {
}
