package wooteco.chess.domain.piece;

import wooteco.chess.domain.piece.movable.*;
import wooteco.chess.domain.position.Position;
import wooteco.chess.domain.position.positions.Positions;

import java.util.List;

public enum PieceType {
    KING(0.0, new UnblockedMovable(MovableDirections.EVERY)),
    QUEEN(9.0, new BlockedMovable(MovableDirections.EVERY)),
    KNIGHT(2.5, new UnblockedMovable(MovableDirections.KNIGHT)),
    ROOK(5.0, new BlockedMovable(MovableDirections.LINEAR)),
    BISHOP(3.0, new BlockedMovable(MovableDirections.DIAGONAL)),
    WHITE_PAWN(1.0, new PawnMovable(MovableDirections.WHITE_PAWN)),
    BLACK_PAWN(1.0, new PawnMovable(MovableDirections.BLACK_PAWN)),
    BLANK(0.0, new UnblockedMovable(MovableDirections.NONE));

    private final double score;
    private final Movable movable;

    PieceType(double score, Movable movable) {
        this.score = score;
        this.movable = movable;
    }

    public Positions findMovablePositions(Position position, List<Piece> pieces, Color color) {
        return movable.findMovablePositions(position, pieces, color);
    }

    public double getScore() {
        return score;
    }

    public boolean isKing() {
        return this.equals(KING);
    }

    public boolean isPawn() {
        return this.equals(WHITE_PAWN) || this.equals(BLACK_PAWN);
    }
}
