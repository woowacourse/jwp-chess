package chess.domain.board;

import chess.domain.piece.Blank;
import chess.domain.piece.Piece;
import chess.domain.piece.factory.*;
import chess.domain.position.File;
import chess.domain.position.Position;
import chess.domain.position.Rank;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public final class BoardInitializer {

    private static final List<String> FILE_RANGE = File.fileSymbols();
    private static final List<String> RANK_RANGE = Rank.rankSymbols();

    public static Map<Position, Piece> emptyBoards() {
        final Map<Position, Piece> chessBoard = new TreeMap<>();
        for (final String file : FILE_RANGE) {
            RANK_RANGE.forEach(rank -> chessBoard.put(new Position(file, rank), new Blank(new Position(file, rank))));
        }
        return chessBoard;
    }
}
