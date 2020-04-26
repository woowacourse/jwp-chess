package wooteco.chess.domain.chessGame.gameState;

import wooteco.chess.domain.chessPiece.pieceType.PieceColor;

public abstract class ChessEndState implements GameState {

	protected final PieceColor pieceColor;

	ChessEndState(final PieceColor pieceColor) {
		this.pieceColor = pieceColor;
	}

	@Override
	public boolean isEndState() {
		return true;
	}

	@Override
	abstract public boolean isKingCaughtState();

	@Override
	public PieceColor getPieceColor() {
		return pieceColor;
	}

}
