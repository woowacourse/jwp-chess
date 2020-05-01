package wooteco.chess.domain.piece;

import java.util.function.BiFunction;

import wooteco.chess.domain.piece.implementation.piece.Bishop;
import wooteco.chess.domain.piece.implementation.piece.King;
import wooteco.chess.domain.piece.implementation.piece.Knight;
import wooteco.chess.domain.piece.implementation.piece.Pawn;
import wooteco.chess.domain.piece.implementation.piece.Queen;
import wooteco.chess.domain.piece.implementation.piece.Rook;
import wooteco.chess.domain.player.Team;
import wooteco.chess.domain.position.Position;

public enum PieceType {

    PAWN(1d, Pawn::of),
    KNIGHT(2.5d, Knight::of),
    BISHOP(3d, Bishop::of),
    ROOK(5d, Rook::of),
    QUEEN(9d, Queen::of),
    KING(0d, King::of);

    private double point;
    private BiFunction<Position, Team, PieceState> creator;

    PieceType(final double point, final BiFunction<Position, Team, PieceState> creator) {
        this.point = point;
        this.creator = creator;
    }

    public PieceState apply(Position position, Team team) {
        return creator.apply(position, team);
    }

    public double getPoint() {
        return point;
    }

    public boolean isSameType(PieceType pieceType) {
        return this == pieceType;
    }
}
