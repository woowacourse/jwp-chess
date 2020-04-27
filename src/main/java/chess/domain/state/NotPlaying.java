package chess.domain.state;

import chess.domain.state.exceptions.StateException;
import chess.domain.pieces.Pieces;
import chess.domain.pieces.StartPieces;

public abstract class NotPlaying extends GameState {
	protected NotPlaying(final StateType stateType, final Pieces pieces) {
		super(stateType, pieces);
	}

	@Override
	protected State start() {
		Pieces startPieces = new Pieces(new StartPieces().getInstance());
		return new Started(startPieces);
	}

	@Override
	protected State end() {
		throw new StateException("NotPlaying 상태에서 end 명령어는 유효하지 않습니다.");
	}

	@Override
	protected State move(String from, String to) {
		throw new StateException("NotPlaying 상태에서 move 명령어는 유효하지 않습니다.");
	}

	@Override
	protected State report() {
		return new Reported(pieces);
	}
}
