package chess.domain.chessgame;

import chess.domain.board.Board;
import chess.domain.board.Score;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import chess.exception.IllegalTurnException;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public class ChessGame implements Serializable {

    private final Board board;
    private boolean isPlaying;
    private boolean isBlackTurn;

    public ChessGame() {
        board = new Board();
        board.initChessPieces();
        isPlaying = false;
        isBlackTurn = false;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void move(Position source, Position target) {
        validateTurn(board.piece(source));
        Piece targetPiece = board.piece(target);
        board.move(source, target);
        if (targetPiece.isKing()) {
            exit();
            return;
        }
        changeTurn();
    }

    private void changeTurn() {
        isBlackTurn = !isBlackTurn;
    }

    private void validateTurn(Piece piece) {
        if (piece.isNotEmpty() && piece.isBlack() != isBlackTurn) {
            throw new IllegalTurnException();
        }
    }

    public void start() {
        isPlaying = true;
    }

    public Board board() {
        return board;
    }

    public Map<Position, Piece> pieces() {
        return board.getPieces();
    }

    public Score score(Color color) {
        return board.piecesScore(color);
    }

    public void exit() {
        isPlaying = false;
    }

    public boolean isKingAlive(Color color) {
        return board.isKingAlive(color);
    }

    public void operate(boolean isRestart, boolean isPlaying) {
        if (isRestart) {
            restart();
            return;
        }
        if (isPlaying) {
            this.isPlaying = true;
            return;
        }
        exit();
    }

    private void restart() {
        board.initChessPieces();
        isPlaying = true;
        isBlackTurn = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return isPlaying == chessGame.isPlaying && isBlackTurn == chessGame.isBlackTurn
            && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, isPlaying, isBlackTurn);
    }
}
