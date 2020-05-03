package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.pathStrategy.WhitePawnPathStrategy;

public class WhitePawn extends Pawn {
	public WhitePawn(Position position) {
		super(PieceColor.WHITE, position, new WhitePawnPathStrategy());
	}
}
