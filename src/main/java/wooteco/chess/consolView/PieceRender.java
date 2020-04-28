package wooteco.chess.consolView;

import wooteco.chess.domain.coordinate.File;
import wooteco.chess.domain.piece.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum PieceRender {
    KING(King.class, (piece) -> piece.isBlack() ? "♚" : "♔"),
    QUEEN(Queen.class, (piece) -> piece.isBlack() ? "♛" : "♕"),
    BISHOP(Bishop.class, (piece) -> piece.isBlack() ? "♝" : "♗"),
    KNIGHT(Knight.class, (piece) -> piece.isBlack() ? "♞" : "♘"),
    ROOK(Rook.class, (piece) -> piece.isBlack() ? "♜" : "♖"),
    PAWN(AbstractPawn.class, (piece) -> piece.isBlack() ? "♟" : "♙"),
    BLANK(Blank.class, (piece -> ""));

    private static final Map<Class<? extends Piece>, RenderStrategy> byClass = new HashMap<>();

    static {
        for (PieceRender pieceRender : values()) {
            byClass.put(pieceRender.pieceClass, pieceRender.renderStrategy);
        }
    }

    private final Class<? extends Piece> pieceClass;
    private final RenderStrategy renderStrategy;

    PieceRender(final Class<? extends Piece> pieceClass, final RenderStrategy renderStrategy) {
        this.pieceClass = pieceClass;
        this.renderStrategy = renderStrategy;
    }

    public static String findTokenByPiece(Piece piece) {
        if (byClass.containsKey(piece.getClass())) {
            return byClass.get(piece.getClass()).render(piece);
        }
        return byClass.get(piece.getClass().getSuperclass()).render(piece);
    }

    private boolean isSameType(Piece piece) {
        if (this.pieceClass.equals(piece.getClass())) {
            return true;
        }
        return this.pieceClass.equals(piece.getClass().getSuperclass());
    }
}
