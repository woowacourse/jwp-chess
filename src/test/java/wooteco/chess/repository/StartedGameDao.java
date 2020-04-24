package wooteco.chess.repository;

import java.util.Optional;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.game.Game;
import wooteco.chess.domain.game.Started;

public class StartedGameDao implements GameDao {
	private Game game;

	public StartedGameDao() {
		this.game = new Started(new Board());
		this.game = game.start();
	}

	@Override
	public Optional<Game> findById(int gameId) {
		return Optional.of(game);
	}

	@Override
	public void update(Game game) {
		this.game = game;
	}
}