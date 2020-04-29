package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;

import wooteco.chess.entity.PlayerEntity;

public interface PlayerRepository extends CrudRepository<PlayerEntity, Integer> {
}
