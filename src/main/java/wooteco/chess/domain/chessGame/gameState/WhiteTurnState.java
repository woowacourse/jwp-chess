package wooteco.chess.domain.chessGame.gameState;

import wooteco.chess.domain.chessPiece.pieceType.PieceColor;

public class WhiteTurnState extends ChessTurnState {

	@Override
	public GameState shiftNextTurnState() {
		return new BlackTurnState();
	}

	@Override
	public PieceColor getPieceColor() {
		return PieceColor.WHITE;
	}

}