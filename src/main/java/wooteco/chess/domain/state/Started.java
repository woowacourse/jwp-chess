package wooteco.chess.domain.state;

import static wooteco.chess.domain.piece.Team.*;
import static wooteco.chess.domain.state.StateType.*;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.result.Result;

public class Started extends GameState {
	public Started(Board board) {
		this(board, WHITE);
	}

	public Started(Board board, Team turn) {
		super(board, STARTED, turn);
	}

	@Override
	public GameState start() {
		board.start();
		return new Started(board);
	}

	@Override
	public GameState move(Position from, Position to) {
		validateRightTurn(from);
		board.move(from, to);
		if (board.containsSingleKingWith(turn)) {
			return new KingCatchFinished(board, turn);
		}
		switchTurn();
		return this;
	}

	private void validateRightTurn(Position from) {
		if (board.isNotSameTeamFromPosition(from, turn)) {
			throw new IllegalArgumentException("움직일 수 없는 턴입니다.");
		}
	}

	private void switchTurn() {
		this.turn = turn.getOppositeTeam();
	}

	@Override
	public Result status() {
		return Result.from(board);
	}

	@Override
	public GameState end() {
		return new SuspendFinished(board, turn);
	}

	@Override
	public boolean isNotFinished() {
		return true;
	}
}
