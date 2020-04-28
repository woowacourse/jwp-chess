package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;

import wooteco.chess.entity.GameEntity;

public interface GameRepository extends CrudRepository<GameEntity, String> {
}
