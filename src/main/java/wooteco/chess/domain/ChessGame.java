package wooteco.chess.domain;

import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.state.ChessGameState;
import wooteco.chess.domain.state.Score;

public class ChessGame {
	private ChessGameState state;

	public ChessGame(ChessGameState state) {
		this.state = state;
	}

	public void start() {
		state = state.start();
	}

	public void move(Position source, Position target) {
		state = state.move(source, target);
	}

	public void end() {
		state = state.end();
	}

	public Board board() {
		return state.board();
	}

	public boolean isEnd() {
		return state.isEnd();
	}

	public Turn turn() {
		return state.turn();
	}

	public Status status() {
		Score white = state.score(Team.WHITE);
		Score black = state.score(Team.BLACK);
		return new Status(white, black);
	}
}
