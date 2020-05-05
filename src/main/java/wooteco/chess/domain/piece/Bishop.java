package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.pathStrategy.BishopPathStrategy;

public class Bishop extends Piece {
	private static final String NAME = "b";
	private static final double SCORE = 3;

	public Bishop(PieceColor pieceColor, Position position) {
		super(NAME, SCORE, pieceColor, position, new BishopPathStrategy());
	}
}
