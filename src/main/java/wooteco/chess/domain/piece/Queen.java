package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.pathStrategy.QueenPathStrategy;

public class Queen extends Piece {
	private static final String NAME = "q";
	private static final double SCORE = 9;

	public Queen(PieceColor pieceColor, Position position) {
		super(NAME, SCORE, pieceColor, position, new QueenPathStrategy());
	}
}
