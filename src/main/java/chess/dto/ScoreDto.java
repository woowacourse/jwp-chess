package chess.dto;

import chess.domain.board.Result;
import chess.domain.piece.Team;
import java.util.Map;

public class ScoreDto {

    private final double whiteScore;
    private final double blackScore;

    public ScoreDto(double whiteScore, double blackScore) {
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }

    public static ScoreDto of(Result result) {
        Map<Team, Double> value = result.getValue();
        return new ScoreDto(value.get(Team.WHITE), value.get(Team.BLACK));
    }

    public double getWhiteScore() {
        return whiteScore;
    }

    public double getBlackScore() {
        return blackScore;
    }
}
