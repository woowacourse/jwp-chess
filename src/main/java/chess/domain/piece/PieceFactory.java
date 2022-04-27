package chess.domain.piece;

import chess.domain.chessboard.position.File;
import chess.domain.chessboard.position.Position;
import chess.domain.chessboard.position.Rank;
import chess.domain.game.Player;

import java.util.HashMap;
import java.util.Map;

import static chess.domain.chessboard.position.File.*;
import static chess.domain.chessboard.position.Rank.*;
import static chess.domain.game.Player.*;

public class PieceFactory {

    private static final Map<Position, Piece> INITIAL_BOARD = new HashMap<>();

    static {
        for (final Rank rank : Rank.values()) {
            createBlankIn(rank);
        }
        createBlackPieces();
        createWhitePieces();
    }

    public static Map<Position, Piece> initBoard() {
        return new HashMap<>(INITIAL_BOARD);
    }
    
    private static void createBlankIn(final Rank rank) {
        for (File file : File.values()) {
            INITIAL_BOARD.put(Position.of(rank, file), new Blank(NONE, Symbol.BLANK));
        }
    }

    private static void createBlackPieces() {
        createPieces(EIGHT, BLACK);
        for (final File file : File.values()) {
            INITIAL_BOARD.put(Position.of(SEVEN, file), new Pawn(BLACK, Symbol.PAWN));
        }
    }

    private static void createWhitePieces() {
        createPieces(ONE, WHITE);
        for (final File file : File.values()) {
            INITIAL_BOARD.put(Position.of(TWO, file), new Pawn(WHITE, Symbol.PAWN));
        }
    }

    private static void createPieces(Rank rank, Player player) {
        INITIAL_BOARD.put(Position.of(rank, A), new Rook(player, Symbol.ROOK));
        INITIAL_BOARD.put(Position.of(rank, B), new Knight(player, Symbol.KNIGHT));
        INITIAL_BOARD.put(Position.of(rank, C), new Bishop(player, Symbol.BISHOP));
        INITIAL_BOARD.put(Position.of(rank, D), new Queen(player, Symbol.QUEEN));
        INITIAL_BOARD.put(Position.of(rank, E), new King(player, Symbol.KING));
        INITIAL_BOARD.put(Position.of(rank, F), new Bishop(player, Symbol.BISHOP));
        INITIAL_BOARD.put(Position.of(rank, G), new Knight(player, Symbol.KNIGHT));
        INITIAL_BOARD.put(Position.of(rank, H), new Rook(player, Symbol.ROOK));
    }
}
