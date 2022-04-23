package chess.domain.board;

import chess.domain.board.piece.Color;
import chess.domain.board.piece.Piece;
import chess.domain.board.piece.PieceType;
import chess.domain.board.position.File;
import chess.domain.board.position.Position;
import chess.domain.board.position.Rank;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class BoardFactory {

    private static final Rank BLACK_NON_PAWN_INIT_RANK = Rank.EIGHT;
    private static final Rank BLACK_PAWN_INIT_RANK = Rank.SEVEN;
    private static final Rank WHITE_PAWN_INIT_RANK = Rank.TWO;
    private static final Rank WHITE_NON_PAWN_INIT_RANK = Rank.ONE;

    private static final List<PieceType> NON_PAWNS_BY_INIT_ORDER = List.of(
            PieceType.ROOK, PieceType.KNIGHT, PieceType.BISHOP, PieceType.QUEEN,
            PieceType.KING, PieceType.BISHOP, PieceType.KNIGHT, PieceType.ROOK);

    private BoardFactory() {
    }

    public static Board init() {
        return new Board(initBoardMap());
    }

    static Map<Position, Piece> initBoardMap() {
        final Map<Position, Piece> boardMap = new HashMap<>();
        updateBoard(boardMap, initNonPawns(Color.BLACK, BLACK_NON_PAWN_INIT_RANK));
        updateBoard(boardMap, initPawns(Color.BLACK, BLACK_PAWN_INIT_RANK));
        updateBoard(boardMap, initPawns(Color.WHITE, WHITE_PAWN_INIT_RANK));
        updateBoard(boardMap, initNonPawns(Color.WHITE, WHITE_NON_PAWN_INIT_RANK));
        return boardMap;
    }

    private static List<PositionPiecePair> initPawns(Color color, Rank initRank) {
        return File.allFilesAscending()
                .stream()
                .map(file -> Position.of(file, initRank))
                .map(position -> new PositionPiecePair(position, Piece.of(color, PieceType.PAWN)))
                .collect(Collectors.toUnmodifiableList());
    }

    private static List<PositionPiecePair> initNonPawns(Color color, Rank initRank) {
        List<PositionPiecePair> nonPawns = new ArrayList<>();
        List<File> files = File.allFilesAscending();

        for (int idx = 0; idx < files.size(); idx++) {
            nonPawns.add(new PositionPiecePair(
                    Position.of(files.get(idx), initRank),
                    Piece.of(color, NON_PAWNS_BY_INIT_ORDER.get(idx))));
        }
        return nonPawns;
    }

    private static void updateBoard(Map<Position, Piece> boardMap, List<PositionPiecePair> pieceInfos) {
        for (PositionPiecePair pieceInfo : pieceInfos) {
            Position positionKey = pieceInfo.position;
            Piece pieceValue = pieceInfo.piece;
            boardMap.put(positionKey, pieceValue);
        }
    }

    private static class PositionPiecePair {

        final Position position;
        final Piece piece;

        PositionPiecePair(Position position, Piece piece) {
            this.position = position;
            this.piece = piece;
        }
    }
}
