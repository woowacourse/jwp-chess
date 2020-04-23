package wooteco.chess.domain.chessGame.gameState;

import wooteco.chess.domain.chessPiece.pieceType.PieceColor;

public class KingCaughtState extends ChessEndState {

	KingCaughtState(final PieceColor pieceColor) {
		super(pieceColor);
	}

	@Override
	public boolean isKingCaughtState() {
		return true;
	}

}
