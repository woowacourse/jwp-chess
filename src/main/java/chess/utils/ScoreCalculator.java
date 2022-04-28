package chess.utils;

import chess.domain.Chessboard;
import chess.domain.Position;
import chess.dto.ScoreDto;
import chess.piece.Color;
import chess.piece.Piece;
import chess.piece.Type;

import java.util.Map;

public class ScoreCalculator {

    private static final double MINUS_SCORE_OF_SAME_Y_PAWN = 0.5;
    private static final int NUMBER_OF_PAWNS_FOR_MINUS = 2;

    public static ScoreDto computeScore(Chessboard chessboard) {
        double scoreOfBlack = computeScore(Color.BLACK, chessboard.getBoard());
        double scoreOfWhite = computeScore(Color.WHITE, chessboard.getBoard());

        if (scoreOfBlack > scoreOfWhite) {
            return new ScoreDto(scoreOfBlack, scoreOfWhite, Color.BLACK);
        }
        if (scoreOfBlack < scoreOfWhite) {
            return new ScoreDto(scoreOfBlack, scoreOfWhite, Color.WHITE);
        }

        return new ScoreDto(scoreOfBlack, scoreOfWhite);
    }

    public static double computeScore(Color color, Map<Position, Piece> board) {
        double score = computeTotalScore(color, board);
        score += computeMinusScore(board);
        return score;
    }

    private static double computeTotalScore(Color color, Map<Position, Piece> board) {
        return board.values()
                .stream()
                .filter(piece -> piece.isSameColor(color))
                .mapToDouble(Piece::getScore)
                .sum();
    }

    private static double computeMinusScore(Map<Position, Piece> board) {
        double minusScore = 0;
        for (int i = 0; i < board.size(); i++) {
            minusScore -= computeMinusScoreOfY(i, Color.BLACK,board);
            minusScore -= computeMinusScoreOfY(i, Color.WHITE,board);
        }
        return minusScore;
    }

    private static double computeMinusScoreOfY(int y, Color color, Map<Position, Piece> board) {
        int pawnCount = computePawnCount(y, color, board);
        if (pawnCount >= NUMBER_OF_PAWNS_FOR_MINUS) {
            return pawnCount * MINUS_SCORE_OF_SAME_Y_PAWN;
        }
        return 0;
    }

    private static int computePawnCount(int y, Color color, Map<Position, Piece> board) {
        return (int) board.keySet()
                .stream()
                .filter(position -> position.isSameY(y))
                .map(board::get)
                .filter(piece -> piece.isSameColor(color) && piece.isSameType(Type.PAWN))
                .count();
    }
}
