package chess.dto;

import chess.model.Team;
import java.util.Map;

public class ResultDto {

    private final Map<Team, Double> score;
    private final Team winner;

    public ResultDto(final Map<Team, Double> score, final Team winner) {
        this.score = score;
        this.winner = winner;
    }

    public Map<Team, Double> getScore() {
        return score;
    }

    public Team getWinner() {
        return winner;
    }
}
