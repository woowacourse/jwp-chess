package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.Position;

import java.util.Objects;

public class Piece {
    private final Team team;
    private final PieceType pieceType;
    private boolean hasMoved;

    public Piece(Team team, PieceType pieceType, boolean hasMoved) {
        this.team = team;
        this.pieceType = pieceType;
        this.hasMoved = hasMoved;
    }

    public Piece(Team team, PieceType pieceType) {
        this.team = team;
        this.pieceType = pieceType;
    }

    public static Piece of(char symbol, boolean hasMoved) {
        if (symbol == '.') {
            return new Piece(Team.NONE, PieceType.NONE);
        }
        if (Character.isUpperCase(symbol)) {
            return new Piece(Team.BLACK, PieceType.of(symbol), hasMoved);
        }
        return new Piece(Team.WHITE, PieceType.of(Character.toUpperCase(symbol)), hasMoved);
    }

    public static Piece of(char symbol) {
        if (symbol == '.') {
            return new Piece(Team.NONE, PieceType.NONE);
        }
        if (Character.isUpperCase(symbol)) {
            return new Piece(Team.BLACK, PieceType.of(symbol));
        }
        return new Piece(Team.WHITE, PieceType.of(Character.toUpperCase(symbol)));
    }

    public void throwExceptionIfNotMovable(Board board, Position source, Position target) {
        this.pieceType.validate(board, source, target);
    }

    public void updateHasMoved() {
        this.hasMoved = true;
    }

    public boolean isSameTeam(Piece piece) {
        return this.team == piece.team;
    }

    public boolean isSameTeam(Team team) {
        return this.team == team;
    }

    public boolean isWhite() {
        return this.team == Team.WHITE;
    }

    public boolean isBlack() {
        return this.team == Team.BLACK;
    }

    public boolean isKing() {
        return this.pieceType.isKing();
    }

    public boolean isPawn() {
        return this.pieceType.isPawn();
    }

    public boolean isEmpty() {
        return this.pieceType.isNone();
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public Team getTeam() {
        return this.team;
    }

    public char getSymbol() {
        return team.isBlack() ? pieceType.getUpperCaseInitialCharacter() : pieceType.getLowerCaseInitialCharacter();
    }

    public String getKey() {
        if (isEmpty()) {
            return ".";
        }
        return Character.toString(team.getSymbol()) + getSymbol();
    }

    public boolean getHasMoved() {
        return this.hasMoved;
    }

    public double getScore() {
        return this.pieceType.getScore();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return hasMoved == piece.hasMoved &&
                team == piece.team &&
                pieceType == piece.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, pieceType, hasMoved);
    }
}
