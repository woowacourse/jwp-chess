package chess.controller.dto.response;

import java.util.Map;

public class PlayerScoresResponse {

    private final Map<String, Double> playerScores;

    public PlayerScoresResponse(final Map<String, Double> playerScores) {
        this.playerScores = playerScores;
    }

    public Map<String, Double> getPlayerScores() {
        return playerScores;
    }
}
