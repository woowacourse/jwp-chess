package chess.dto.response;

import chess.model.Team;
import java.util.Map;

public class ResultResponse {

    private final Map<Team, Double> score;
    private final Team winner;

    public ResultResponse(final Map<Team, Double> score, final Team winner) {
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
