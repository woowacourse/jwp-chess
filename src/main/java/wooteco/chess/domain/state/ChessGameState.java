package wooteco.chess.domain.state;

import wooteco.chess.domain.Board;
import wooteco.chess.domain.Team;
import wooteco.chess.domain.Turn;
import wooteco.chess.domain.position.Position;

// 구현한 애들-board + turn()
public interface ChessGameState {

	boolean isEnd();

	ChessGameState start();

	ChessGameState move(Position source, Position target);

	ChessGameState end();

	Board board();

	Score score(Team team);

	Turn turn();
}
