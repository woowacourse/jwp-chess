package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.pathStrategy.BlackPawnPathStrategy;

public class BlackPawn extends Pawn {
	public BlackPawn(Position position) {
		super(PieceColor.BLACK, position, new BlackPawnPathStrategy());
	}
}
