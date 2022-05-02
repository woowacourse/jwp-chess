package chess.domain.game;

import chess.domain.board.Board;
import chess.domain.game.score.ScoreResult;
import chess.domain.game.state.BlackTurn;
import chess.domain.game.state.GameState;
import chess.domain.game.state.WhiteTurn;
import chess.domain.piece.PieceColor;
import chess.domain.position.Position;

public class ChessGame {

    private GameState state;

    private ChessGame(GameState gameState) {
        this.state = gameState;
    }

    public static ChessGame of(Board board, PieceColor pieceColor) {
        if (pieceColor.equals(PieceColor.WHITE)) {
            return new ChessGame(new WhiteTurn(board));
        }

        return new ChessGame(new BlackTurn(board));
    }

    public ScoreResult getStatus() {
        return state.status();
    }

    public void movePiece(Position from, Position to) {
        this.state = state.move(from, to);
    }

    public boolean isWhiteTurn() {
        return state.isWhiteTurn();
    }

    public boolean isEnd() {
        boolean hasWhiteKing = getBoard().hasKing(PieceColor.WHITE);
        boolean hasBlackKing = getBoard().hasKing(PieceColor.BLACK);

        return !(hasWhiteKing && hasBlackKing);
    }

    public Board getBoard() {
        return state.getBoard();
    }

    public PieceColor getWinColor() {
        return state.getWinColor();
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "state=" + state +
                '}';
    }
}
