package wooteco.chess.utils;

import wooteco.chess.Board;
import wooteco.chess.piece.Piece;
import wooteco.chess.position.File;
import wooteco.chess.position.Position;
import wooteco.chess.position.Rank;

import java.util.Map;

public class BoardConverter {

    public static String convertToString(Board board) {
        Map<Position, Piece> pieces = board.getPieces();
        StringBuilder sb = new StringBuilder();
        for(Rank rank : Rank.valuesExceptNone()){
            for(File file : File.valuesExceptNone()){
                Piece piece = pieces.get(Position.of(file, rank));
                sb.append(piece.getSymbol());
            }
        }
        return sb.toString();
    }
}
