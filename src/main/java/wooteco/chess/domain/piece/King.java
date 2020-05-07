package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.pathStrategy.KingPathStrategy;

public class King extends Piece {
	private static final String NAME = "k";
	private static final double SCORE = 0;

	public King(PieceColor pieceColor, Position position) {
		super(NAME, SCORE, pieceColor, position, new KingPathStrategy());
	}
}
