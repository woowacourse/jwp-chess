package wooteco.chess.domain.game;

import static wooteco.chess.domain.piece.Team.*;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.result.Result;

public class Started extends Game {
	public Started(Board board) {
		this(board, WHITE);
	}

	public Started(Board board, Team turn) {
		super(board, turn);
	}

	@Override
	public Game start() {
		board.start();
		return new Started(board);
	}

	@Override
	public Game move(Position from, Position to) {
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
	public Game end() {
		return new SuspendFinished(board, turn);
	}

	@Override
	public boolean isNotFinished() {
		return true;
	}

	@Override
	public String getStateType() {
		return "started";
	}
}
