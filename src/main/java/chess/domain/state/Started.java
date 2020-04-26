package chess.domain.state;

import chess.domain.pieces.Pieces;

public class Started extends Playing {
	public Started(Pieces pieces) {
		super(StateType.STARTED, pieces);
	}
}
