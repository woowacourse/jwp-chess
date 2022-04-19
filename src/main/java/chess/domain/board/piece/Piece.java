package chess.domain.board.piece;

import chess.domain.board.position.Position;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Piece {

    private static final String INVALID_ATTACK_TARGET_EXCEPTION_MESSAGE = "공격할 수 없는 대상입니다.";

    protected final Color color;
    protected final PieceType type;

    protected Piece(Color color, PieceType type) {
        this.color = color;
        this.type = type;
    }

    public static Piece of(Color color, PieceType type) {
        return PieceCache.getCache(color, type);
    }

    public abstract boolean canMove(Position from, Position to);

    public final boolean canAttack(Position from, Position to, Piece targetPiece) {
        if (targetPiece.hasColorOf(color)) {
            throw new IllegalArgumentException(INVALID_ATTACK_TARGET_EXCEPTION_MESSAGE);
        }
        return isAttackableRoute(from, to);
    }

    protected abstract boolean isAttackableRoute(Position from, Position to);

    public final boolean hasColorOf(Color color) {
        return this.color == color;
    }

    public final boolean hasTypeOf(PieceType type) {
        return this.type == type;
    }

    public final double toScore() {
        return type.getScore();
    }

    @Override
    public String toString() {
        return "Piece{" + color + " " + type + '}';
    }

    private static class PieceCache {

        static Map<List<?>, Piece> pieceCache = new HashMap<>();
        static Piece getCache(Color color, PieceType pieceType) {
            return pieceCache.computeIfAbsent(toKey(color, pieceType), (unused) -> initCacheOf(color, pieceType));
        }

        private static List<Object> toKey(Color color, PieceType pieceType) {
            return List.of(color, pieceType, 1);
        }

        static Piece initCacheOf(Color color, PieceType type) {
            if (type == PieceType.PAWN) {
                return new Pawn(color);
            }
            return new NonPawn(color, type);
        }
    }
}
