package wooteco.chess.domain.game;

import static wooteco.chess.domain.piece.Team.*;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.BoardFactory;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.result.Result;

public class Ready extends Game {
	public static final Ready READY_INSTANCE = new Ready(BoardFactory.ofEmpty(), NONE);

	public Ready(Board board, Team turn) {
		super(board, turn);
	}

	@Override
	public Game start() {
		board.start();
		return new Started(board);
	}

	@Override
	public Game move(Position from, Position to) {
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
	public Game end() {
		return new SuspendFinished(board, turn);
	}

	@Override
	public String getStateType() {
		return "ready";
	}
}
