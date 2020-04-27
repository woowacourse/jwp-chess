package spring.chess.result;

import spring.chess.exception.InvalidConstructorValueException;
import spring.chess.score.Score;

import java.util.Objects;

public class ChessScores {
    private final Score whiteScore;
    private final Score blackScore;

    public ChessScores(Score whiteScore, Score blackScore) {
        validNullValue(whiteScore, blackScore);
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }

    private void validNullValue(Score whiteScore, Score blackScore) {
        if (Objects.isNull(whiteScore) || Objects.isNull(blackScore)) {
            throw new InvalidConstructorValueException();
        }
    }

    public Score getWhiteScore() {
        return whiteScore;
    }

    public Score getBlackScore() {
        return blackScore;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessScores that = (ChessScores) o;
        return Objects.equals(whiteScore, that.whiteScore) &&
                Objects.equals(blackScore, that.blackScore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(whiteScore, blackScore);
    }
}
