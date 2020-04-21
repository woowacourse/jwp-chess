package wooteco.chess.domain.state;

import static wooteco.chess.domain.state.StateType.*;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.piece.Team;
import wooteco.chess.domain.result.Result;

public class SuspendFinished extends Finished {
	public SuspendFinished(Board board, Team turn) {
		super(board, SUSPEND_FINISHED, turn);
	}

	@Override
	public Team getWinner() {
		Result status = status();
		return status.findWinner();
	}
}
