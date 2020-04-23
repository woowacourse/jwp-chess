package wooteco.chess.domain.chessGame.gameState;

import wooteco.chess.domain.chessPiece.pieceType.PieceColor;

public class BlackTurnState extends ChessTurnState {

	@Override
	public GameState shiftNextTurnState() {
		return new WhiteTurnState();
	}

	@Override
	public PieceColor getPieceColor() {
		return PieceColor.BLACK;
	}

}
