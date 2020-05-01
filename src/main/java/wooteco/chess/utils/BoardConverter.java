package wooteco.chess.utils;

import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.File;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.board.Rank;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;

import java.util.HashMap;
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

    public static Board convertToBoard(String boardInformation, boolean isWhite) {
        Map<Position, Piece> pieces = new HashMap<>();
        char[] symbols = boardInformation.toCharArray();
        int index = 0;
        for (Rank rank : Rank.valuesExceptNone()) {
            for (File file : File.valuesExceptNone()) {
                pieces.put(Position.of(file, rank), Piece.of(symbols[index++]));
            }
        }
        return new Board(pieces, Team.of(isWhite));
    }
}
