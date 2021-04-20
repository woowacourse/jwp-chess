package chess.domain.board;

import chess.domain.chess.Color;

public class ScoreDto {
    private final double blackScore;
    private final double whiteScore;

    public ScoreDto(Board board) {
        this(board.score(Color.BLACK), board.score(Color.WHITE));
    }

    public ScoreDto(double blackScore, double whiteScore) {
        this.blackScore = blackScore;
        this.whiteScore = whiteScore;
    }
}
