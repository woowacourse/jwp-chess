package wooteco.chess.domain.game;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Team;

public class KingCatchFinished extends Finished {
	public KingCatchFinished(Board board, Team turn) {
		super(board, turn);
		validCountOfKing(board);
	}

	private void validCountOfKing(Board board) {
		if (board.containsNotSingleKingWith(turn)) {
			throw new IllegalArgumentException("체스 보드 위에 킹은 하나만 있어야 합니다.");
		}
	}

	@Override
	public Team getWinner() {
		return turn;
	}

	@Override
	public String getStateType() {
		return "kingCatchFinish";
	}
}
