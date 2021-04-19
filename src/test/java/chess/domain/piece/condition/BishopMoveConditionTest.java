package chess.domain.piece.condition;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.Board;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Position;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BishopMoveConditionTest {

    @DisplayName("비숍이 조건대로 움직이는지 확인한다.")
    @Test
    void isSatisfyBy() {
        BishopMoveCondition bishopMoveCondition = new BishopMoveCondition();
        Board board = new Board(
                Arrays.asList(
                        Piece.createBishop(Color.WHITE, 0, 0)
                )
        );
        boolean rightActual = bishopMoveCondition.isSatisfyBy(board, Piece.createBishop(Color.WHITE, 0, 0),
                new Position(1, 1));
        boolean falseActual = bishopMoveCondition.isSatisfyBy(board, Piece.createBishop(Color.WHITE, 0, 0),
                new Position(1, 0));

        assertThat(rightActual).isTrue();
        assertThat(falseActual).isFalse();
    }

    @DisplayName("비숍의 이동 경로에 장애물이 있으면 예외")
    @Test
    void isSatisfyBy_FalseCase() {
        BishopMoveCondition bishopMoveCondition = new BishopMoveCondition();
        Board board = new Board(
                Arrays.asList(
                        Piece.createBishop(Color.WHITE, 0, 0),
                        Piece.createPawn(Color.WHITE, 1, 1)
                )
        );

        boolean actual = bishopMoveCondition.isSatisfyBy(board, Piece.createBishop(Color.WHITE, 0, 0),
                new Position(2, 2));

        assertThat(actual).isFalse();
    }
}