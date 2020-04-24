package wooteco.chess.repository;

import java.util.Optional;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.game.Game;
import wooteco.chess.domain.game.Started;

public class FinishedDrawGameDao implements GameDao {

	@Override
	public Optional<Game> findById(int gameId) {
		Game game = new Started(new Board());
		game = game.start();
		game = game.end();
		return Optional.of(game);
	}

	@Override
	public void update(Game game) {

	}
}