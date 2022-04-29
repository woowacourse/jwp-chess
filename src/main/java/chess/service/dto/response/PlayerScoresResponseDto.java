package chess.service.dto.response;

import java.util.Map;

public class PlayerScoresResponseDto {

    private final Map<String, Double> playerScores;

    public PlayerScoresResponseDto(final Map<String, Double> playerScores) {
        this.playerScores = playerScores;
    }

    public Map<String, Double> getPlayerScores() {
        return playerScores;
    }
}
