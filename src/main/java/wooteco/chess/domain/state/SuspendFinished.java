package wooteco.chess.domain.state;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.result.Result;

public class SuspendFinished extends Finished {
	public SuspendFinished(Board board, Team turn) {
		super(board, turn);
	}

	@Override
	public Team getWinner() {
		Result status = status();
		return status.findWinner();
	}

	@Override
	public String getStateType() {
		return "suspendFinish";
	}
}
