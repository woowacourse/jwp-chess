package chess.dto.response;

import chess.domain.board.Score;

public class ScoreResponseDto {

    private final double score;

    public ScoreResponseDto(Score score) {
        this.score = score.getValue();
    }

    public double getScore() {
        return score;
    }
}
