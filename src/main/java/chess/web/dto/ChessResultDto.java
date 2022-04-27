package chess.web.dto;

import chess.domain.Result;

public class ChessResultDto {

    private final int gameId;
    private final double blackScore;
    private final double whiteScore;
    private final Result winner;

    public ChessResultDto(int gameId, double blackScore, double whiteScore, Result winner) {
        this.gameId = gameId;
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
        this.winner = winner;
    }

    public int getGameId() {
        return gameId;
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
