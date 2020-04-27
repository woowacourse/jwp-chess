package wooteco.chess.dao.repository;

import org.springframework.data.repository.CrudRepository;

import wooteco.chess.domain.Player;

public interface PlayerRepository extends CrudRepository<Player, Long> {
}
