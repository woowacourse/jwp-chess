package wooteco.chess.spark.webutil;


import wooteco.chess.domain.board.Board;
import wooteco.chess.domain.board.Position;
import wooteco.chess.domain.piece.Piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

public class ModelParser {

    private static final String BLANK = "blank";
    private static final String MOVABLE = "movable";
    private static final String MARK = "_mark";

    public static Map<String, Object> parseBlankBoard() {
        return Position.positions
                .stream()
                .collect(toMap(Position::toString, position -> BLANK));
    }

    public static Map<String, Object> parseBoard(final Board board) {
        Map<String, Object> output = new HashMap<>();

        for (Position position : Position.positions) {
            Piece piece = board.findPieceOn(position);
            output.put(position.toString(), piece.toString());
        }

        output.putAll(parseMovablePlaces(new ArrayList<>()));

        return output;
    }

    public static Map<String, Object> parseBoard(final Board board, final List<Position> movablePlaces) {
        Map<String, Object> output = new HashMap<>();

        for (Position position : Position.positions) {
            Piece piece = board.findPieceOn(position);
            output.put(position.toString(), piece.toString());
        }

        output.putAll(parseMovablePlaces(movablePlaces));

        return output;
    }

    public static Map<String, Object> parseMovablePlaces(final List<Position> markingPlaces) {
        return Position.positions
                .stream()
                .collect(toMap(ModelParser::parseMarkPosition, position -> parseMovable(position, markingPlaces)));
    }

    private static String parseMarkPosition(final Position position) {
        return position.toString() + MARK;
    }

    private static String parseMovable(final Position position, final List<Position> positions) {
        if (positions.contains(position)) {
            return MOVABLE;
        }
        return BLANK;
    }
}
