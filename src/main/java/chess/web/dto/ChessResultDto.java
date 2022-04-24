package chess.web.dto;

import chess.domain.Result;

public class ChessResultDto {

    private final double blackScore;
    private final double whiteScore;
    private final Result winner;

    public ChessResultDto(double blackScore, double whiteScore, Result winner) {
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
        this.winner = winner;
    }

    public double getBlackScore() {
        return blackScore;
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public Result getWinner() {
        return winner;
    }
}
