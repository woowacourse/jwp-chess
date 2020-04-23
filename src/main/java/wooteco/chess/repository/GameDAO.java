package wooteco.chess.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import wooteco.chess.domain.game.Game;

@Repository
public interface GameDAO {
	Optional<Game> findById(int gameId);

	void update(Game game);
}

