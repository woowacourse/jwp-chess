package wooteco.chess.domain.piece;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.Position;

import java.util.Objects;

public class Piece {
    private final Team team;
    private final PieceType pieceType;

    public Piece(Team team, PieceType pieceType) {
        this.team = team;
        this.pieceType = pieceType;
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

    public boolean isPawn() {
        return this.pieceType.isPawn();
    }

    public boolean isEmpty() {
        return this.pieceType.isNone();
    }

    public Team getTeam() {
        return this.team;
    }

    public String getTeamSymbol() {
        return Character.toString(team.getSymbol());
    }

    public char getSymbol() {
        return team.isBlack() ? pieceType.getUpperCaseInitialCharacter() : pieceType.getLowerCaseInitialCharacter();
    }

    public double getScore() {
        return this.pieceType.getScore();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return team == piece.team &&
                pieceType == piece.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, pieceType);
    }
}
