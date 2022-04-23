package chess.dto;

import chess.domain.Winner;
import java.util.Objects;

public class ResultDto {
    private final String scoreOfWhite;
    private final String scoreOfBlack;
    private final String winner;

    public ResultDto(String scoreOfWhite, String scoreOfBlack, String winner) {
        this.scoreOfWhite = scoreOfWhite;
        this.scoreOfBlack = scoreOfBlack;
        this.winner = winner;
    }

    public static ResultDto of(double statusOfWhite, double statusOfBlack, Winner winner) {
        return new ResultDto(String.format("%.1f", statusOfWhite), String.format("%.1f", statusOfBlack), winner.name());
    }

    public String getScoreOfWhite() {
        return scoreOfWhite;
    }

    public String getScoreOfBlack() {
        return scoreOfBlack;
    }

    public String getWinner() {
        return winner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ResultDto resultDto = (ResultDto) o;
        return Objects.equals(scoreOfWhite, resultDto.scoreOfWhite) && Objects
                .equals(scoreOfBlack, resultDto.scoreOfBlack) && Objects.equals(winner, resultDto.winner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scoreOfWhite, scoreOfBlack, winner);
    }
}
