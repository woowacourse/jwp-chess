package chess.domain;

import chess.domain.piece.detail.Team;

public class Result {

    private final Double blackScore;
    private final Double whiteScore;
    private final Team winner;

    public Result(final Double blackScore, final Double whiteScore, final Team winner) {
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
        this.winner = winner;
    }

    public static Result byScore(final Double blackScore, final Double whiteScore) {
        return new Result(blackScore, whiteScore, calculateWinner(blackScore, whiteScore));
    }

    private static Team calculateWinner(final Double blackScore, final Double whiteScore) {
        if (blackScore > whiteScore) {
            return Team.BLACK;
        }
        if (whiteScore > blackScore) {
            return Team.WHITE;
        }
        return Team.NONE;
    }

    public Double getBlackScore() {
        return blackScore;
    }

    public Double getWhiteScore() {
        return whiteScore;
    }

    public Team getWinner() {
        return winner;
    }
}
