package chess.model.piece;

import chess.dao.PieceEntity;
import chess.model.Color;
import chess.model.piece.pawn.Pawn;
import java.util.Arrays;
import java.util.function.Function;

public enum PieceType {
    KING(King.class, "k", King::new),
    QUEEN(Queen.class, "q", Queen::new),
    KNIGHT(Knight.class, "n", Knight::new),
    ROOK(Rook.class, "r", Rook::new),
    BISHOP(Bishop.class, "b", Bishop::new),
    PAWN(Pawn.class, "p", Pawn::of),
    EMPTY(Empty.class, ".", color -> new Empty());

    private final Class<? extends Piece> pieceClass;
    private final String letter;
    private final Function<Color, Piece> function;

    PieceType(Class<? extends Piece> pieceClass, String letter, Function<Color, Piece> function) {
        this.pieceClass = pieceClass;
        this.letter = letter;
        this.function = function;
    }

    public static String getLetterByColor(Piece piece) {
        return convertByColor(piece, getLetter(piece));
    }

    private static String convertByColor(Piece piece, String letter) {
        if (piece.isBlack()) {
            return letter.toUpperCase();
        }
        return letter;
    }

    private static String getLetter(Piece piece) {
        return getStringByPieceLetter(piece, pieceType -> pieceType.letter);
    }

    private static String getStringByPieceLetter(Piece piece, Function<PieceType, String> letterMapping) {
        return Arrays.stream(values())
                .filter(pieceType -> pieceType.pieceClass.isAssignableFrom(piece.getClass()))
                .map(letterMapping)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("표현할 수 없는 기물이 파악됐습니다. " + piece.getClass()));
    }

    public static String getName(Piece piece) {
        return getStringByPieceLetter(piece, Enum::name);
    }

    public static Piece createPiece(PieceEntity pieceDto) {
        PieceType pieceType = valueOf(pieceDto.getType().toUpperCase());
        Color color = Color.valueOf(pieceDto.getColor().toUpperCase());
        return pieceType.function.apply(color);
    }
}
