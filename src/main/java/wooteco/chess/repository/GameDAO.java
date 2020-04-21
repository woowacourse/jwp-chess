package wooteco.chess.repository;

import java.util.Optional;

import wooteco.chess.domain.game.Game;

public interface GameDAO {
	Optional<Game> findById(int gameId);

	void update(Game game);
}

