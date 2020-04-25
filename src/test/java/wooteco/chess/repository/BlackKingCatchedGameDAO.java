package wooteco.chess.repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.game.Game;
import wooteco.chess.domain.game.KingCatchFinished;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.PieceFactory;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;

public class BlackKingCatchedGameDAO implements GameDAO {

	@Override
	public Optional<Game> findById(int gameId) {
		Map<Position, Piece> maps = new HashMap<>();
		maps.put(Position.of("a3"), PieceFactory.of("b"));
		maps.put(Position.of("a5"), PieceFactory.of("k"));
		maps.put(Position.of("a7"), PieceFactory.of("Q"));
		Game game = new KingCatchFinished(new Board(maps), Team.WHITE);
		return Optional.of(game);
	}

	@Override
	public void update(Game game) {

	}
}