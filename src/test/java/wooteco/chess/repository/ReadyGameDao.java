package wooteco.chess.repository;

import java.util.Optional;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.game.Game;
import wooteco.chess.domain.game.Ready;

public class ReadyGameDao implements GameDao {

	@Override
	public Optional<Game> findById(int gameId) {
		return Optional.of(new Ready(new Board()));
	}

	@Override
	public void update(Game game) {

	}
}