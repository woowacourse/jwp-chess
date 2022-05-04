package chess.dto;

import java.util.Map;
import java.util.stream.Collectors;

import chess.domain.Color;

public class ScoreResponse {

    private final Map<String, Double> colorScore;

    public ScoreResponse() {
        this(null);
    }

    public ScoreResponse(Map<String, Double> colorScore) {
        this.colorScore = colorScore;
    }

    public static ScoreResponse of(Map<Color, Double> colorScore) {
        return new ScoreResponse(
            colorScore.entrySet()
                .stream()
                .collect(Collectors.toMap(
                    entry -> entry.getKey().name(),
                    Map.Entry::getValue))
        );
    }

    public Map<String, Double> getColorScore() {
        return colorScore;
    }
}
