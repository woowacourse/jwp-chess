package chess.domain.piece.factory;

import static chess.domain.piece.PieceTeam.BLACK;
import static chess.domain.piece.PieceTeam.EMPTY;
import static chess.domain.piece.PieceTeam.WHITE;

import chess.domain.piece.Bishop;
import chess.domain.piece.EmptySpace;
import chess.domain.piece.King;
import chess.domain.piece.Knight;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.piece.Queen;
import chess.domain.piece.Rook;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

public class PieceFactory {

    private static final Map<PieceNames, Supplier<? extends Piece>> pieceCreator;

    static {
        pieceCreator = initCreator();
    }

    private static class PieceNames {
        private final List<String> values;

        public PieceNames(String... values) {
            this.values = List.of(values);
        }

        public boolean contains(String pieceName) {
            return values.contains(pieceName);
        }
    }

    private static Map<PieceNames, Supplier<? extends Piece>> initCreator() {
        Map<PieceNames, Supplier<? extends Piece>> pieceCreator = new HashMap<>();

        initWhitePiece(pieceCreator);
        initBlackPiece(pieceCreator);
        initEmptySpace(pieceCreator);

        return pieceCreator;
    }

    private static void initWhitePiece(Map<PieceNames, Supplier<? extends Piece>> pieceCreator) {
        pieceCreator.put(new PieceNames("p", "whitePawn"), () -> new Pawn(WHITE));
        pieceCreator.put(new PieceNames("r", "whiteRook"), () -> new Rook(WHITE));
        pieceCreator.put(new PieceNames("n", "whiteKnight"), () -> new Knight(WHITE));
        pieceCreator.put(new PieceNames("b", "whiteBishop"), () -> new Bishop(WHITE));
        pieceCreator.put(new PieceNames("q", "whiteQueen"), () -> new Queen(WHITE));
        pieceCreator.put(new PieceNames("k", "whiteKing"), () -> new King(WHITE));
    }

    private static void initBlackPiece(Map<PieceNames, Supplier<? extends Piece>> pieceCreator) {
        pieceCreator.put(new PieceNames("P", "blackPawn"), () -> new Pawn(BLACK));
        pieceCreator.put(new PieceNames("R", "blackRook"), () -> new Rook(BLACK));
        pieceCreator.put(new PieceNames("N", "blackKnight"), () -> new Knight(BLACK));
        pieceCreator.put(new PieceNames("B", "blackBishop"), () -> new Bishop(BLACK));
        pieceCreator.put(new PieceNames("Q", "blackQueen"), () -> new Queen(BLACK));
        pieceCreator.put(new PieceNames("K", "blackKing"), () -> new King(BLACK));
    }

    private static void initEmptySpace(Map<PieceNames, Supplier<? extends Piece>> pieceCreator) {
        pieceCreator.put(new PieceNames("."), () -> new EmptySpace(EMPTY));
    }

    public static Piece create(String pieceName) {

        for (Entry<PieceNames, Supplier<? extends Piece>> entry : pieceCreator.entrySet()) {
            final PieceNames pieceNames = entry.getKey();
            final Supplier<? extends Piece> createdPieceSupplier = entry.getValue();
            if (pieceNames.contains(pieceName)) {
                return createdPieceSupplier.get();
            }
        }

        throw new IllegalArgumentException("[ERROR] piece 를 찾지 못하였습니다");
    }
}
