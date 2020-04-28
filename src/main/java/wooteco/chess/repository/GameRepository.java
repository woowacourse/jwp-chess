package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import wooteco.chess.domain.entity.Game;

public interface GameRepository extends CrudRepository<Game, Long> {
}
