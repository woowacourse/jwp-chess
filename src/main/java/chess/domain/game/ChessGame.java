package chess.domain.game;

import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;

import java.util.Collections;
import java.util.Map;

public class ChessGame {

    private final ChessBoard chessBoard;
    private Color turn;

    public ChessGame() {
        this(new ChessBoard(), Color.WHITE);
    }

    public ChessGame(ChessBoard chessBoard, Color turn) {
        this.chessBoard = chessBoard;
        this.turn = turn;
    }

    public void move(Position source, Position target) {
        validateTurn(source);
        chessBoard.move(source, target);
        turn = turn.getOppositeColor();
    }

    public boolean isOver() {
        return chessBoard.isOver();
    }

    public void validateTurn(Position source) {
        if (turn != chessBoard.getPiece(source).getColor()) {
            throw new IllegalArgumentException("자신의 턴을 확인하세요.");
        }
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public Map<Position, Piece> getChessBoardMap() {
        return Collections.unmodifiableMap(chessBoard.getChessBoard());
    }

    public Piece getPiece(Position position) {
        return chessBoard.getPiece(position);
    }

    public String getTurn() {
        return turn.toString();
    }

    public double getScore(Color color) {
        return chessBoard.score(color);
    }
}
