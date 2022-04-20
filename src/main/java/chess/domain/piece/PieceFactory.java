package chess.domain.piece;

import chess.domain.piece.detail.PieceType;
import chess.domain.piece.detail.Team;
import chess.domain.piece.multiplemove.Bishop;
import chess.domain.piece.multiplemove.Queen;
import chess.domain.piece.multiplemove.Rook;
import chess.domain.piece.pawn.Pawn;
import chess.domain.piece.singlemove.King;
import chess.domain.piece.singlemove.Knight;
import chess.domain.square.Square;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.BiFunction;

public class PieceFactory {

    private final static Map<PieceType, BiFunction<Team, Square, Piece>> pieceSupplier =
            new EnumMap<>(PieceType.class) {{
                put(PieceType.PAWN, Pawn::of);
                put(PieceType.ROOK, Rook::new);
                put(PieceType.KNIGHT, Knight::new);
                put(PieceType.BISHOP, Bishop::new);
                put(PieceType.QUEEN, Queen::new);
                put(PieceType.KING, King::new);
                put(PieceType.BLANK, Blank::new);
            }};

    public static Piece createPiece(final PieceType pieceType, final Team team, final Square square) {
        return pieceSupplier.get(pieceType).apply(team, square);
    }
}
