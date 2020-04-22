package wooteco.chess.utils;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.File;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.board.Rank;
import wooteco.chess.domain.piece.Piece;

import java.util.Map;

public class BoardConverter {

    public static String convertToString(Board board) {
        Map<Position, Piece> pieces = board.getPieces();
        StringBuilder sb = new StringBuilder();
        for (Rank rank : Rank.valuesExceptNone()) {
            for (File file : File.valuesExceptNone()) {
                Piece piece = pieces.get(Position.of(file, rank));
                sb.append(piece.getSymbol());
            }
        }
        return sb.toString();
    }
}
