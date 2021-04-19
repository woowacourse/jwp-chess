package chess.domain.piece.condition;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.Board;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Position;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FirstTurnBlackPawnMoveConditionTest {

    @DisplayName("검은 폰의 첫 움직임을 확인한다.")
    @Test
    void isSatisfyBy() {
        FirstTurnBlackPawnMoveCondition condition = new FirstTurnBlackPawnMoveCondition();
        Board board = new Board(Collections.singletonList(
                Piece.createPawn(Color.BLACK, 7, 0)
        ));
        boolean rightActual = condition.isSatisfyBy(board, Piece.createPawn(Color.BLACK, 7, 0),
                new Position(5, 0));
        boolean falseActual = condition.isSatisfyBy(board, Piece.createPawn(Color.BLACK, 7, 0),
                new Position(4, 0));

        assertThat(rightActual).isTrue();
        assertThat(falseActual).isFalse();
    }

    @DisplayName("검은 폰의 첫 이동경로에 장애물이 있으면 안된다.")
    @Test
    void isSatisfyBy_false() {
        FirstTurnBlackPawnMoveCondition condition = new FirstTurnBlackPawnMoveCondition();
        Board board = new Board(Arrays.asList(
                Piece.createPawn(Color.BLACK, 6, 0),
                Piece.createPawn(Color.BLACK, 5, 0)
        ));
        boolean actual = condition.isSatisfyBy(board, Piece.createPawn(Color.BLACK, 6, 0),
                new Position(4, 0));

        assertThat(actual).isFalse();
    }

}