package chess.dto.response;

import chess.model.Team;
import java.util.Map;

public class ResultResponse {

    private final Map<String, Double> score;
    private final String winner;

    public ResultResponse(final Map<String, Double> score, final String winner) {
        this.score = score;
        this.winner = winner;
    }

    public Map<String, Double> getScore() {
        return score;
    }

    public String getWinner() {
        return winner;
    }
}
