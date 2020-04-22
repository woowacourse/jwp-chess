package wooteco.chess.domain.state;

import static wooteco.chess.domain.piece.Team.*;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.result.Result;

public class Ready extends GameState {
	public Ready(Board board) {
		this(board, NONE);
	}

	public Ready(Board board, Team turn) {
		super(board, turn);
	}

	@Override
	public GameState start() {
		board.start();
		return new Started(board);
	}

	@Override
	public GameState move(Position from, Position to) {
		throw new UnsupportedOperationException("게임을 시작해야 합니다.");
	}

	@Override
	public Result status() {
		throw new UnsupportedOperationException("게임을 시작해야 합니다.");
	}

	@Override
	public boolean isNotFinished() {
		return true;
	}

	@Override
	public GameState end() {
		return new SuspendFinished(board, turn);
	}

	@Override
	public String getStateType() {
		return "ready";
	}
}
