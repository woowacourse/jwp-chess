package chess.domain.board;

import static chess.domain.board.Board.*;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.position.Column;
import chess.domain.position.Position;

public class BoardResult {

    private final Map<Position, Piece> board;

    public BoardResult(Map<Position, Piece> board) {
        this.board = board;
    }

    public Map<Position, Piece> pieces(Color color) {
        return board.entrySet()
            .stream()
            .filter(entry -> entry.getValue().isNotEmpty() && entry.getValue().isAlly(color))
            .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
    }

    public Score piecesScore(Color color) {
        Score score = new Score();
        Map<Position, Piece> pieces = pieces(color);

        for (Entry<Position, Piece> entry : pieces.entrySet()) {
            score = score.sum(entry.getValue().getScore());
        }
        score = minusPawnScore(score, color);
        return score;
    }

    private Score minusPawnScore(Score score, Color color) {
        int minusCount = 0;
        for (int row = 0; row < CHESS_BOARD_SIZE; row++) {
            minusCount += rowAllyPawnCount(row, color);
        }
        return score.minusPawnScore(minusCount);
    }

    private int rowAllyPawnCount(int column, Color color) {
        int count = (int)pieces(color).entrySet()
            .stream()
            .filter(entry -> entry.getValue().isPawn()
                && entry.getKey().isColumnEquals(new Column(column)))
            .count();

        if (count >= PAWN_ALLY_COUNT_CONDITION) {
            return count;
        }
        return 0;
    }

    public boolean isKingAlive(Color color) {
        return pieces(color).entrySet().stream()
            .anyMatch(entry -> entry.getValue().isKing());
    }

}
