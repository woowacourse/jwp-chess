package chess.dto;

import chess.domain.piece.Color;
import java.util.Map;

public class StatusDto {

    private String whiteScore;
    private String blackScore;

    public StatusDto(String whiteScore, String blackScore) {
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;
    }

    public StatusDto(Map<Color, Double> scores) {
        this(String.valueOf(scores.get(Color.WHITE)), String.valueOf(scores.get(Color.BLACK)));
    }

    public String getWhiteScore() {
        return whiteScore;
    }

    public String getBlackScore() {
        return blackScore;
    }
}
