package chess.domain.piece.pawn;

import chess.domain.piece.NonBlankPiece;
import chess.domain.piece.Piece;
import chess.domain.piece.detail.Direction;
import chess.domain.piece.detail.PieceType;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;
import java.util.List;

public abstract class Pawn extends NonBlankPiece {

    protected Pawn(final Team team, final Square square) {
        super(team, square);
    }

    public static Pawn of(final Team team, final Square square) {
        if (team.isNone()) {
            throw new IllegalArgumentException("폰 기물의 팀은 NONE일 수 없습니다.");
        }
        if (team.isBlack()) {
            return new BlackPawn(team, square);
        }
        return new WhitePawn(team, square);
    }

    protected abstract boolean isPawnInitial();

    protected abstract List<Direction> getPawnAttackDirections();

    @Override
    public Piece moveTo(final Square to) {
        return Pawn.of(getTeam(), to);
    }

    @Override
    protected boolean isMovable(final Piece target) {
        final Square to = target.getSquare();
        final List<Direction> directions = getAvailableDirections();
        final Direction direction = directions.get(0);

        if (target.isBlank()) {
            return isAvailableMoveToBlank(to, direction);
        }

        return isAttackable(target);
    }

    @Override
    public PieceType getPieceType() {
        return PieceType.PAWN;
    }

    private boolean isAvailableMoveToBlank(final Square to, final Direction direction) {
        if (isFirstTurn() && isFirstAvailableMove(to, direction)) {
            return true;
        }

        return this.square.next(direction).equals(to);
    }

    private boolean isFirstTurn() {
        return isPawnInitial();
    }

    private boolean isFirstAvailableMove(final Square to, final Direction direction) {
        final Square firstMove = square.next(direction, 2);
        return firstMove.equals(to);
    }

    private boolean isAttackable(final Piece target) {
        final List<Direction> directions = getPawnAttackDirections();
        final Square to = target.getSquare();

        return directions.stream()
                .filter(direction -> this.square.isExist(direction))
                .map(direction -> this.square.next(direction))
                .anyMatch(to::equals);
    }
}
