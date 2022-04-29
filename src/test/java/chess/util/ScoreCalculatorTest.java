package chess.util;

import chess.domain.Position;
import chess.piece.*;
import chess.utils.ScoreCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ScoreCalculatorTest {
    @Test
    @DisplayName("올바르게 점수를 계산하는지 확인")
    void computeScore() {
        Map<Position, Piece> board = Map.of(
                new Position(0, 1), new Queen(Color.BLACK),
                new Position(0, 2), new Rook(Color.BLACK),
                new Position(0, 3), new Bishop(Color.BLACK),
                new Position(0, 4), new Knight(Color.BLACK),
                new Position(0, 5), new Pawn(Color.BLACK)
        );

        assertThat(ScoreCalculator.computeScore(Color.BLACK, board))
                .isEqualTo(20.5);
    }

    @Test
    @DisplayName("같은 세로줄에 같은 폰이 있는 경우 0.5점으로 계산하는지 확인")
    void computeScorePawn() {
        Map<Position, Piece> board = Map.of(
                new Position(1, 0), new Pawn(Color.BLACK),
                new Position(2, 0), new Pawn(Color.BLACK),
                new Position(3, 0), new Pawn(Color.BLACK)
        );

        assertThat(ScoreCalculator.computeScore(Color.BLACK, board))
                .isEqualTo(1.5);
    }
}
