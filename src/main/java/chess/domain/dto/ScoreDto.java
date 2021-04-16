package chess.domain.dto;

public class ScoreDto {
    private String scoreMessage;

    public ScoreDto() {
    }

    private ScoreDto(String scoreMessage) {
        this.scoreMessage = scoreMessage;
    }

    public static ScoreDto of(String scoreMessage) {
        return new ScoreDto(scoreMessage);
    }

    public String getScoreMessage() {
        return scoreMessage;
    }
}
