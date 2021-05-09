package chess.domain.board.piece;

import chess.domain.board.position.Path;
import chess.domain.board.position.Position;
import chess.domain.direction.Direction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Piece {
    private final Owner owner;
    private final String symbol;
    private final List<Direction> directions;

    public Piece(Owner owner, String symbol, List<Direction> directions) {
        this.owner = owner;
        this.symbol = symbol;
        this.directions = directions;
    }

    public List<Path> movablePath(final Position source) {
        return directions.stream()
                .map(direction -> movablePathDirection(source, direction))
                .collect(Collectors.toList());
    }

    private Path movablePathDirection(final Position source, final Direction direction) {
        List<Position> path = new ArrayList<>();
        Position target = source;
        while (target.isValidPosition(direction) && isValidDistance(source, target.next(direction))) {
            target = target.next(direction);
            path.add(target);
        }
        return Path.of(path);
    }

    private boolean isValidDistance(final Position source, final Position target) {
        return source.getDistance(target) <= this.maxDistance();
    }

    public boolean isDifferentTeam(final Piece other) {
        return this.owner.isDifferent(other.owner);
    }

    public boolean isSameOwner(final Owner owner) {
        return this.owner.isSame(owner);
    }

    public boolean isSameOwnerPawn(final Owner owner) {
        return this.isPawn() && this.owner.isSame(owner);
    }

    public boolean isPawn() {
        return false;
    }

    public boolean isOwnersKing(Owner owner) {
        return false;
    }

    public boolean isEmptyPiece() {
        return false;
    }

    public final boolean isEnemy(final Piece other) {
        return this.owner.isEnemy(other.owner);
    }

    public String getSymbol() {
        return this.symbol;
    }

    ;

    public abstract Score score();

    public abstract int maxDistance();

    public abstract boolean isReachable(final Position source, final Position target, final Piece targetPiece);
}
