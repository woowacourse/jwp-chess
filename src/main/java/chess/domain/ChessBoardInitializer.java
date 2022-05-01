package chess.domain;

import static chess.domain.piece.Color.BLACK;
import static chess.domain.piece.Color.WHITE;
import static chess.domain.piece.Name.BISHOP;
import static chess.domain.piece.Name.KING;
import static chess.domain.piece.Name.KNIGHT;
import static chess.domain.piece.Name.PAWN;
import static chess.domain.piece.Name.QUEEN;
import static chess.domain.piece.Name.ROOK;
import static chess.domain.piece.PieceStorage.valueOf;
import static chess.domain.position.File.A;
import static chess.domain.position.File.B;
import static chess.domain.position.File.C;
import static chess.domain.position.File.D;
import static chess.domain.position.File.E;
import static chess.domain.position.File.F;
import static chess.domain.position.File.G;
import static chess.domain.position.File.H;
import static chess.domain.position.Rank.EIGHT;
import static chess.domain.position.Rank.ONE;
import static chess.domain.position.Rank.SEVEN;
import static chess.domain.position.Rank.TWO;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.HashMap;
import java.util.Map;

public class ChessBoardInitializer {
    private static final Map<Position, Piece> INIT_BOARD = new HashMap<>();

    static {
        INIT_BOARD.put(new Position(A, ONE), valueOf(ROOK, WHITE));
        INIT_BOARD.put(new Position(B, ONE), valueOf(BISHOP, WHITE));
        INIT_BOARD.put(new Position(C, ONE), valueOf(BISHOP, WHITE));
        INIT_BOARD.put(new Position(D, ONE), valueOf(QUEEN, WHITE));
        INIT_BOARD.put(new Position(E, ONE), valueOf(KING, WHITE));
        INIT_BOARD.put(new Position(F, ONE), valueOf(BISHOP, WHITE));
        INIT_BOARD.put(new Position(G, ONE), valueOf(KNIGHT, WHITE));
        INIT_BOARD.put(new Position(H, ONE), valueOf(ROOK, WHITE));
        INIT_BOARD.put(new Position(A, TWO), valueOf(PAWN, WHITE));
        INIT_BOARD.put(new Position(B, TWO), valueOf(PAWN, WHITE));
        INIT_BOARD.put(new Position(C, TWO), valueOf(PAWN, WHITE));
        INIT_BOARD.put(new Position(D, TWO), valueOf(PAWN, WHITE));
        INIT_BOARD.put(new Position(E, TWO), valueOf(PAWN, WHITE));
        INIT_BOARD.put(new Position(F, TWO), valueOf(PAWN, WHITE));
        INIT_BOARD.put(new Position(G, TWO), valueOf(PAWN, WHITE));
        INIT_BOARD.put(new Position(H, TWO), valueOf(PAWN, WHITE));

        INIT_BOARD.put(new Position(A, EIGHT), valueOf(ROOK, BLACK));
        INIT_BOARD.put(new Position(B, EIGHT), valueOf(BISHOP, BLACK));
        INIT_BOARD.put(new Position(C, EIGHT), valueOf(BISHOP, BLACK));
        INIT_BOARD.put(new Position(D, EIGHT), valueOf(QUEEN, BLACK));
        INIT_BOARD.put(new Position(E, EIGHT), valueOf(KING, BLACK));
        INIT_BOARD.put(new Position(F, EIGHT), valueOf(BISHOP, BLACK));
        INIT_BOARD.put(new Position(G, EIGHT), valueOf(KNIGHT, BLACK));
        INIT_BOARD.put(new Position(H, EIGHT), valueOf(ROOK, BLACK));
        INIT_BOARD.put(new Position(A, SEVEN), valueOf(PAWN, BLACK));
        INIT_BOARD.put(new Position(B, SEVEN), valueOf(PAWN, BLACK));
        INIT_BOARD.put(new Position(C, SEVEN), valueOf(PAWN, BLACK));
        INIT_BOARD.put(new Position(D, SEVEN), valueOf(PAWN, BLACK));
        INIT_BOARD.put(new Position(E, SEVEN), valueOf(PAWN, BLACK));
        INIT_BOARD.put(new Position(F, SEVEN), valueOf(PAWN, BLACK));
        INIT_BOARD.put(new Position(G, SEVEN), valueOf(PAWN, BLACK));
        INIT_BOARD.put(new Position(H, SEVEN), valueOf(PAWN, BLACK));
    }

    private ChessBoardInitializer() {
    }

    public static Map<Position, Piece> getInitBoard() {
        return INIT_BOARD;
    }
}
