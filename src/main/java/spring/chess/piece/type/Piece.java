package spring.chess.piece.type;

import spring.chess.board.Route;
import spring.chess.exception.InvalidConstructorValueException;
import spring.chess.piece.type.movable.PieceMovable;
import spring.chess.score.Score;
import spring.chess.team.Team;

import java.util.Objects;

public abstract class Piece {
    public final char name;
    protected final Score score;
    private final PieceMovable pieceMovable;

    Piece(char name, Score score, Team team, PieceMovable pieceMovable) {
        validNullValue(score, team, pieceMovable);
        this.name = changeName(team, name);
        this.score = score;
        this.pieceMovable = pieceMovable;
    }

    private void validNullValue(Score score, Team team, PieceMovable pieceMovable) {
        if (Objects.isNull(score) || Objects.isNull(team) || Objects.isNull(pieceMovable)) {
            throw new InvalidConstructorValueException();
        }
    }

    private static char changeName(Team team, char name) {
        if (team.isBlack()) {
            return Character.toUpperCase(name);
        }
        return name;
    }

    public boolean canMove(Route route) {
        return pieceMovable.canMove(route);
    }

    public boolean isSameTeam(Team team) {
        return getTeam() == team;
    }

    public boolean isNotSame(Team team) {
        return getTeam() != team;
    }

    public boolean isNotSameTeam(Piece piece) {
        return getTeam() != piece.getTeam();
    }

    public boolean isKing() {
        return this.getClass().equals(King.class);
    }

    public boolean isPawn() {
        return this.getClass().equals(Pawn.class);
    }

    public boolean isReverseTeam(Piece piece) {
        Team anotherTeam = piece.getTeam();
        Team myTeam = this.getTeam();
        return myTeam.isReverseTeam(anotherTeam);
    }

    public Team getTeam() {
        if (Character.isUpperCase(name)) {
            return Team.BLACK;
        }
        return Team.WHITE;
    }

    public char getName() {
        return name;
    }

    public Score getScore() {
        return score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return name == piece.name &&
                Objects.equals(score, piece.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, score);
    }

    @Override
    public String toString() {
        return String.valueOf(name);
    }
}
