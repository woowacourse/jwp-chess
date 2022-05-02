package chess.controller.response;

import chess.domain.Score;
import chess.domain.piece.PieceColor;

public class ScoreResponse {

    private final String name;
    private final double score;

    private ScoreResponse(String name, double score) {
        this.name = name;
        this.score = score;
    }

    public static ScoreResponse of(PieceColor color, Score score) {
        return new ScoreResponse(color.name(), score.getValue());
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }
}
