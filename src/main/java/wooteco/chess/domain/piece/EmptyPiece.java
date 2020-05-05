package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.pathStrategy.EmptyPathStrategy;

public class EmptyPiece extends Piece {
	private static final String NAME = ".";
	private static final double SCORE = 0;

	public EmptyPiece(PieceColor pieceColor, Position position) {
		super(NAME, SCORE, pieceColor, position, new EmptyPathStrategy());
	}
}
