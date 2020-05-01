package wooteco.chess.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import wooteco.chess.entity.GameEntity;

public interface GameRepository extends CrudRepository<GameEntity, String> {
    @Override
    List<GameEntity> findAll();
}
