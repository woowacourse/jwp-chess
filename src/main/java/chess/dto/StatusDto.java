package chess.dto;

public class StatusDto {

    private String whiteScore;
    private String blackScore;

    public StatusDto(String whiteScore, String blackScore) {
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }

    public StatusDto(Double whiteScore, Double blackScore) {
        this(String.valueOf(whiteScore), String.valueOf(blackScore));
    }

    public String getWhiteScore() {
        return whiteScore;
    }

    public String getBlackScore() {
        return blackScore;
    }
}
