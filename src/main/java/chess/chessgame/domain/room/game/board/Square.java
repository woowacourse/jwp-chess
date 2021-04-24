package chess.chessgame.domain.room.game.board;

import chess.chessgame.domain.room.game.board.order.MoveOrder;
import chess.chessgame.domain.room.game.board.piece.Blank;
import chess.chessgame.domain.room.game.board.piece.Piece;
import chess.chessgame.domain.room.game.board.piece.attribute.Color;
import chess.chessgame.domain.room.game.board.position.Position;
import chess.chessgame.domain.room.game.board.position.Rank;

import java.util.NoSuchElementException;
import java.util.Objects;

public class Square {
    private final Position position;

    private Piece piece;

    public Square(Position position, Piece piece) {
        this.position = position;
        this.piece = piece;
    }

    public void move(MoveOrder moveOrder) {
        if (hasNotPiece()) {
            throw new NoSuchElementException("해당 위치엔 말이 없습니다.");
        }

        if (piece.canMove(moveOrder)) {
            moveOrder.getTo().piece = this.piece;
            this.piece = Blank.getInstance();
            return;
        }
        throw new IllegalArgumentException("기물이 움직일 수 없는 상황입니다.");
    }

    public boolean kindOf(Class<? extends Piece> kind) {
        return kind.isInstance(getPiece());
    }

    public Piece getPiece() {
        return piece;
    }

    public boolean hasPiece() {
        return piece.isNotBlank();
    }

    public boolean hasNotPiece() {
        return !piece.isNotBlank();
    }

    public Position getPosition() {
        return position;
    }

    public Color getColor() {
        return getPiece().getColor();
    }

    public String getNotationText() {
        return piece.getNotationText();
    }

    public boolean isSameRank(Rank rank) {
        return this.position.isSameRank(rank);
    }

    public boolean isSamePosition(Position position) {
        return this.position.equals(position);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Square)) return false;
        Square square = (Square) o;
        return Objects.equals(getPosition(), square.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosition());
    }
}
