package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;

import wooteco.chess.entity.GameEntity;

import java.util.List;

public interface GameRepository extends CrudRepository<GameEntity, String> {
    @Override
    List<GameEntity> findAll();
}
