package chess.model.board;

import static java.util.stream.Collectors.toMap;

import chess.model.Color;
import chess.model.piece.Piece;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Score {
    private static final int PAWN_POINT_DIVIDE_VALUE = 2;

    private final Map<Color, Double> scores;

    private Score(final Map<Color, Double> scores) {
        this.scores = new HashMap<>(scores);
    }

    public static Score of(final Map<Square, Piece> board) {
        Map<Color, Double> scores = Color.getPlayerColors()
                .stream()
                .collect(toMap(Function.identity(), color -> calculatePoint(board, color)));
        return new Score(scores);
    }

    private static double calculatePoint(final Map<Square, Piece> board, final Color color) {
        return board.entrySet().stream()
                .filter(entry -> entry.getValue().isSameColor(color))
                .mapToDouble(entry -> calculateEachPoint(board, entry.getKey()))
                .sum();
    }

    private static double calculateEachPoint(final Map<Square, Piece> board, final Square square) {
        Piece piece = board.get(square);
        if (piece.isPawn() && hasAllyPawnInSameFile(board, square, piece)) {
            return piece.getPointValue() / PAWN_POINT_DIVIDE_VALUE;
        }
        return piece.getPointValue();
    }

    private static boolean hasAllyPawnInSameFile(final Map<Square, Piece> board, final Square sourceSquare,
                                                 final Piece sourcePiece) {
        return board.keySet().stream()
                .filter(square -> sourceSquare.isSameFile(square) && sourceSquare.isDifferent(square))
                .map(board::get)
                .anyMatch(piece -> piece.isPawn() && piece.isAlly(sourcePiece));
    }

    public Color findWinnerByScore() {
        double blackPoint = scores.get(Color.BLACK);
        double whitePoint = scores.get(Color.WHITE);
        if (blackPoint < whitePoint) {
            return Color.WHITE;
        }
        if (blackPoint > whitePoint) {
            return Color.BLACK;
        }
        return Color.NOTHING;
    }

    public Map<Color, Double> getScoresPerColor() {
        return Map.copyOf(scores);
    }
}
