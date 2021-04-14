package chess.domain.board;

import chess.domain.piece.Blank;
import chess.domain.piece.Piece;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class BoardInitializer {

    private static final List<LocationInitializer> locationInitializers;
    private static final List<String> FILE_RANGE = File.fileSymbols();
    private static final List<String> RANK_RANGE = Rank.rankSymbols();

    static {
        locationInitializers = Arrays.asList(new PawnInitializer(), new RookInitializer(), new KingInitializer(),
            new QueenInitializer(), new BishopInitializer(), new KnightInitializer());
    }

    public static Map<Position, Piece> initializeBoard() {
        final Map<Position, Piece> chessBoard = emptyBoard();
        locationInitializers.forEach(initializer -> chessBoard.putAll(initializer.initialize()));
        return chessBoard;
    }

    public static Map<Position, Piece> emptyBoard() {
        final Map<Position, Piece> chessBoard = new TreeMap<>();
        for (final String file : FILE_RANGE) {
            RANK_RANGE.forEach(rank -> chessBoard.put(new Position(file, rank), new Blank(new Position(file, rank))));
        }
        return chessBoard;
    }
}
