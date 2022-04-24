package chess.model.game;

import chess.model.Color;
import chess.model.board.Score;
import chess.model.board.Square;
import chess.model.piece.King;
import chess.model.piece.Piece;
import java.util.Map;

public class GameResult {
    private static final int VALID_KING_COUNT = 2;

    private final Score score;
    private final Color winnerColor;

    private GameResult(Score score, Color winnerColor) {
        this.score = score;
        this.winnerColor = winnerColor;
    }

    public static GameResult from(Map<Square, Piece> board, Score score) {
        return new GameResult(score, findWinner(board, score));
    }

    private static Color findWinner(Map<Square, Piece> board, Score score) {
        if (bothKingAlive(board)) {
            return score.findWinnerByScore();
        }
        return findWinnerByKing(board);
    }

    public static boolean bothKingAlive(Map<Square, Piece> board) {
        return board.keySet().stream()
                .map(board::get)
                .filter(Piece::isKing)
                .count() == VALID_KING_COUNT;
    }

    public static Color findWinnerByKing(Map<Square, Piece> board) {
        return getColorHasKing(board);
    }

    private static Color getColorHasKing(Map<Square, Piece> board) {
        return Color.getPlayerColors().stream()
                .filter(color -> board.containsValue(new King(color)))
                .findFirst()
                .orElse(Color.NOTHING);
    }

    public Map<Color, Double> getScore() {
        return score.getScoresPerColor();
    }

    public Color getWinnerColor() {
        return winnerColor;
    }
}
