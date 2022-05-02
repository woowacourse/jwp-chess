package chess.dto.response;

import chess.domain.game.Score;
import chess.domain.piece.Team;

public class StatusResponseDto {

    private final double whiteScore;
    private final double blackScore;
    private final String winningTeam;

    private StatusResponseDto(final double whiteScore, final double blackScore, final String winningTeam) {
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
        this.winningTeam = winningTeam;
    }

    public static StatusResponseDto of(final Score score) {
        double whiteScore = score.calculateScore(Team.WHITE);
        double blackScore = score.calculateScore(Team.BLACK);
        return new StatusResponseDto(whiteScore, blackScore, score.calculateWinningTeam(whiteScore, blackScore).getValue());
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public double getBlackScore() {
        return blackScore;
    }

    public String getWinningTeam() {
        return winningTeam;
    }
}
