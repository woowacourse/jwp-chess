package chess.util;

import chess.model.domain.piece.Piece;
import chess.model.domain.piece.PieceFactory;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PieceLetterConverter {

    private static final Map<PieceFactory, String> PIECES_LETTER;

    static {
        Map<PieceFactory, String> piecesLetter = new HashMap<>();
        piecesLetter.put(PieceFactory.BLACK_KING, "♚");
        piecesLetter.put(PieceFactory.WHITE_KING, "♔");
        piecesLetter.put(PieceFactory.BLACK_QUEEN, "♛");
        piecesLetter.put(PieceFactory.WHITE_QUEEN, "♕");
        piecesLetter.put(PieceFactory.BLACK_BISHOP, "♝");
        piecesLetter.put(PieceFactory.WHITE_BISHOP, "♗");
        piecesLetter.put(PieceFactory.BLACK_KNIGHT, "♞");
        piecesLetter.put(PieceFactory.WHITE_KNIGHT, "♘");
        piecesLetter.put(PieceFactory.BLACK_ROOK, "♜");
        piecesLetter.put(PieceFactory.WHITE_ROOK, "♖");
        piecesLetter.put(PieceFactory.BLACK_PAWN, "♟");
        piecesLetter.put(PieceFactory.WHITE_PAWN, "♙");
        PIECES_LETTER = Collections.unmodifiableMap(piecesLetter);
    }

    public static String convertToLetter(Piece piece) {
        Objects.requireNonNull(piece, "convert할 piece는 null일 수 없습니다.");
        return PIECES_LETTER.keySet().stream()
            .filter(pieceFactory -> pieceFactory.isSame(piece))
            .map(PIECES_LETTER::get)
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(piece + " 피스는 컨버트 불가합니다."));
    }
}
