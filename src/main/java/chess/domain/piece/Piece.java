package chess.domain.piece;

import java.util.List;

import chess.domain.piece.detail.Direction;
import chess.domain.piece.detail.PieceType;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;

public abstract class Piece {

    private final Team team;
    protected Square square;

    protected Piece(final Team team, final Square square) {
        this.team = team;
        this.square = square;
    }

    public abstract Piece moveTo(final Square to);

    public abstract boolean isBlank();

    public abstract PieceType getPieceType();

    protected abstract boolean isMovable(final Piece piece);

    protected abstract List<Direction> getAvailableDirections();

    public boolean canMove(final Piece piece) {
        validateMove(piece);
        return isMovable(piece);
    }

    public void updateSquare(final Square to) {
        square = to;
    }

    public boolean isSameTeam(final Piece target) {
        return team == target.team;
    }

    public boolean isSameTeam(final Team team) {
        return this.team == team;
    }

    public boolean isKing() {
        return false;
    }

    public boolean isBlack() {
        return team.isBlack();
    }

    public boolean isWhite() {
        return team.isWhite();
    }

    private void validateMove(final Piece piece) {
        if (this.isSameTeam(piece)) {
            throw new IllegalArgumentException("같은 팀 위치로는 이동할 수 없습니다.");
        }
    }

    public Team getTeam() {
        return team;
    }

    public Square getSquare() {
        return square;
    }

    public double getScore() {
        return getPieceType().getScore();
    }
}
