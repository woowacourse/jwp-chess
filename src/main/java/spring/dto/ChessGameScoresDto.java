package spring.dto;

import spring.chess.result.ChessScores;
import spring.chess.score.Score;

public class ChessGameScoresDto {
    private final Score whiteScore;
    private final Score blackScore;

    public ChessGameScoresDto(ChessScores chessScores) {
        whiteScore = chessScores.getWhiteScore();
        blackScore = chessScores.getBlackScore();
    }
}
