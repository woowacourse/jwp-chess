package chess.domain.statistics;

import chess.domain.piece.Color;
import chess.exception.DomainException;

import java.util.Arrays;
import java.util.function.BiPredicate;

public enum MatchResult {
    DRAW(Double::equals),
    WHITE_WIN((whiteScore, blackScore) -> whiteScore > blackScore),
    BLACK_WIN((whiteScore, blackScore) -> whiteScore < blackScore);

    private final BiPredicate<Double, Double> matchResultConditionScore;

    MatchResult(BiPredicate<Double, Double> matchResultConditionScore) {
        this.matchResultConditionScore = matchResultConditionScore;
    }

    public static MatchResult generateMatchResult(double whiteScore, double blackScore) {
        return Arrays.stream(values())
                .filter(matchResult -> matchResult.matchResultConditionScore.test(whiteScore, blackScore))
                .findAny()
                .orElseThrow(() -> new DomainException("조건에 맞는 승패 결과가 없습니다."));
    }

    public static MatchResult generateMatchResultByColor(Color color) {
        if (color == Color.WHITE) {
            return WHITE_WIN;
        }
        return BLACK_WIN;
    }
}
