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

class NormalWhitePawnMoveConditionTest {

    @DisplayName("흰색 폰의 기본 움직임을 확인한다.")
    @Test
    void isSatisfyBy() {
        NormalWhitePawnMoveCondition condition = new NormalWhitePawnMoveCondition();
        Board board = new Board(Collections.singletonList(
                Piece.createPawn(Color.WHITE, 0, 0)
        ));
        boolean rightActual = condition.isSatisfyBy(board, Piece.createPawn(Color.WHITE, 0, 0),
                new Position(1, 0));
        boolean falseActual = condition.isSatisfyBy(board, Piece.createPawn(Color.WHITE, 0, 0),
                new Position(2, 0));

        assertThat(rightActual).isTrue();
        assertThat(falseActual).isFalse();
    }

    @DisplayName("흰색 폰의 이동 경로에 장애물이 있는지 확인")
    @Test
    void isSatisfyBy_false() {
        NormalWhitePawnMoveCondition condition = new NormalWhitePawnMoveCondition();
        Board board = new Board(Arrays.asList(
                Piece.createPawn(Color.WHITE, 0, 0),
                Piece.createPawn(Color.WHITE, 1, 0)

        ));
        boolean actual = condition.isSatisfyBy(board, Piece.createPawn(Color.WHITE, 0, 0),
                new Position(1, 0));

        assertThat(actual).isFalse();
    }

}