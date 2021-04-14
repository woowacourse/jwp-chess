package chess.dto;

import chess.domain.board.Score;

public class ScoreResponseDto {

    private double score;

    public ScoreResponseDto(Score score) {
        this.score = score.getValue();
    }

    public double getScore() {
        return score;
    }
}
