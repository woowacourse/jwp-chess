package wooteco.chess.domain.chessGame.gameState;

import wooteco.chess.domain.chessPiece.pieceType.PieceColor;

public class EndState extends ChessEndState {

	EndState(final PieceColor pieceColor) {
		super(pieceColor);
	}

	@Override
	public boolean isKingCaughtState() {
		return false;
	}

}
