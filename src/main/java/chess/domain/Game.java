package chess.domain;

import chess.domain.game.ChessBoard;
import chess.domain.pieces.Color;
import chess.domain.pieces.Piece;
import chess.domain.position.Position;
import java.util.Optional;

public final class Game {

    private final ChessBoard chessBoard;
    private Color turn;

    public Game(ChessBoard chessBoard, Color turn) {
        this.chessBoard = chessBoard;
        this.turn = turn;
    }

    public Optional<Piece> piece(final Position position) {
        return chessBoard.piece(position);
    }

    public void move(final Position source, final Position target) {
        validateNotEquals(source, target);
        validateCorrectTurn(source);
        chessBoard.move(source, target);
        changeTurn();
    }

    private void validateNotEquals(final Position sourcePosition, final Position targetPosition) {
        if (sourcePosition.equals(targetPosition)) {
            throw new IllegalArgumentException("출발지와 목적지가 동일할 수 없습니다.");
        }
    }

    private void validateCorrectTurn(final Position sourcePosition) {
        final Optional<Piece> wrappedPiece = chessBoard.piece(sourcePosition);
        if (wrappedPiece.isPresent() && !wrappedPiece.get().isSameColor(turn)) {
            throw new IllegalArgumentException("지금은 " + turn.value() + "의 턴입니다.");
        }
    }

    private void changeTurn() {
        turn = Color.opposite(turn);
    }

    public Double calculateScore(Color color) {
        return chessBoard.calculateScore(color);
    }

    public boolean isEnd() {
        return chessBoard.isEnd();
    }

    public ChessBoard getChessBoard() {
        return chessBoard;
    }

    public Color getTurn() {
        return turn;
    }
}