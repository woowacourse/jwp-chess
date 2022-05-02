package chess.dto.response;

import chess.domain.GameResult;

public class PlayerScoreDto {

    private final double whitePlayerScore;
    private final double blackPlayerScore;
    private final String whitePlayerResult;
    private final String blackPlayerResult;

    private PlayerScoreDto(double whitePlayerScore, double blackPlayerScore, String whitePlayerResult,
            String blackPlayerResult) {
        this.whitePlayerScore = whitePlayerScore;
        this.blackPlayerScore = blackPlayerScore;
        this.whitePlayerResult = whitePlayerResult;
        this.blackPlayerResult = blackPlayerResult;
    }

    public static PlayerScoreDto of(final GameResult whitePlayer, final GameResult blackPlayer) {
        final double whitePlayerScore = whitePlayer.getPlayerScore();
        final double blackPlayerScore = blackPlayer.getPlayerScore();
        final String whitePlayerResult = whitePlayer.getPlayerResult();
        final String blackPlayerResult = blackPlayer.getPlayerResult();
        return new PlayerScoreDto(whitePlayerScore, blackPlayerScore, whitePlayerResult, blackPlayerResult);
    }

    public double getWhitePlayerScore() {
        return whitePlayerScore;
    }

    public double getBlackPlayerScore() {
        return blackPlayerScore;
    }

    public String getWhitePlayerResult() {
        return whitePlayerResult;
    }

    public String getBlackPlayerResult() {
        return blackPlayerResult;
    }
}
