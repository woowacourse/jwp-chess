package chess.domain;

import chess.domain.piece.Color;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import chess.domain.position.File;
import chess.domain.position.Position;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class Score {

    private static final BigDecimal PAWN_DEDUCT_POINT = new BigDecimal("0.5");
    private static final BigDecimal INIT_SCORE = new BigDecimal("38.0");

    private final BigDecimal value;

    public Score(BigDecimal value) {
        this.value = value;
    }

    public Score(String value) {
        this.value = new BigDecimal(value);
    }

    public Score() {
        this(INIT_SCORE);
    }

    public Score(Map<Position, Piece> board, Color color) {
        this(getDefaultScore(board, color).subtract(getDeductPointOfPawn(board, color)));
    }

    private static BigDecimal getDefaultScore(Map<Position, Piece> board, Color color) {
        return board.values().stream()
                .filter(piece -> piece.isSameColor(color))
                .map(Piece::getPoint)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private static BigDecimal getDeductPointOfPawn(Map<Position, Piece> board, Color color) {
        return PAWN_DEDUCT_POINT.multiply(new BigDecimal(numberOfPawn(board, color)));
    }

    private static long numberOfPawn(Map<Position, Piece> board, Color color) {
        return Arrays.stream(File.values())
                .map(file -> numberOfEachFilePawn(board, color, file))
                .filter(numberOfPawn -> numberOfPawn >= 2)
                .reduce(0L, Long::sum);
    }

    private static long numberOfEachFilePawn(Map<Position, Piece> board, Color color, File file) {
        return board.keySet().stream()
                .filter(position -> position.isSameFile(file))
                .map(board::get)
                .filter(piece -> piece.equals(new Pawn(color)))
                .filter(piece -> piece.isSameColor(color))
                .count();
    }

    public BigDecimal getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Score score = (Score) o;
        return Objects.equals(value, score.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
