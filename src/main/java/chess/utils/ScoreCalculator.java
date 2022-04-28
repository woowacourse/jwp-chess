package chess.utils;

import chess.domain.Chessboard;
import chess.dto.ScoreDto;
import chess.piece.Color;

public class ScoreCalculator {
    private static final double MINUS_SCORE_OF_SAME_Y_PAWN = 0.5;

    public static ScoreDto computeScore(Chessboard chessboard) {
        double scoreOfBlack = chessboard.computeScore(Color.BLACK, MINUS_SCORE_OF_SAME_Y_PAWN);
        double scoreOfWhite = chessboard.computeScore(Color.WHITE, MINUS_SCORE_OF_SAME_Y_PAWN);

        if (scoreOfBlack > scoreOfWhite) {
            return new ScoreDto(scoreOfBlack, scoreOfWhite, Color.BLACK);
        }
        if (scoreOfBlack < scoreOfWhite) {
            return new ScoreDto(scoreOfBlack, scoreOfWhite, Color.WHITE);
        }

        return new ScoreDto(scoreOfBlack, scoreOfWhite);
    }
}
