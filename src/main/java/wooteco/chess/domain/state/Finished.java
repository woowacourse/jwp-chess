package wooteco.chess.domain.state;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.result.Result;

public abstract class Finished extends GameState {
	public Finished(Board board, Team turn) {
		super(board, turn);
	}

	@Override
	public GameState start() {
		board.start();
		return new Started(board);
	}

	@Override
	public GameState move(Position from, Position to) {
		throw new UnsupportedOperationException("이미 게임이 끝났습니다.");
	}

	@Override
	public Result status() {
		return Result.from(board);
	}

	@Override
	public GameState end() {
		throw new UnsupportedOperationException("이미 게임이 끝났습니다.");
	}

	@Override
	public boolean isNotFinished() {
		return false;
	}
}
