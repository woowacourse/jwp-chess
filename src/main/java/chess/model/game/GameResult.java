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

    private GameResult(final Score score, final Color winnerColor) {
        this.score = score;
        this.winnerColor = winnerColor;
    }

    public static GameResult from(final Map<Square, Piece> board, final Score score) {
        return new GameResult(score, findWinner(board, score));
    }

    private static Color findWinner(final Map<Square, Piece> board, final Score score) {
        if (bothKingAlive(board)) {
            return score.findWinnerByScore();
        }
        return findWinnerByKing(board);
    }

    private static boolean bothKingAlive(final Map<Square, Piece> board) {
        return board.keySet().stream()
                .map(board::get)
                .filter(Piece::isKing)
                .count() == VALID_KING_COUNT;
    }

    private static Color findWinnerByKing(final Map<Square, Piece> board) {
        return getColorHavingKing(board);
    }

    private static Color getColorHavingKing(final Map<Square, Piece> board) {
        return Color.getPlayerColors().stream()
                .filter(color -> board.containsValue(new King(color)))
                .findFirst()
                .orElse(Color.NOTHING);
    }

    public Map<Color, Double> getScore() {
        return Map.copyOf(score.getScoresPerColor());
    }

    public Color getWinnerColor() {
        return winnerColor;
    }
}
