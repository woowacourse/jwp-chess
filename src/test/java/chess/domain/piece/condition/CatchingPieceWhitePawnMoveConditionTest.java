package chess.domain.piece.condition;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.Board;
import chess.domain.piece.Color;
import chess.domain.piece.Piece;
import chess.domain.piece.Position;
import java.util.Arrays;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CatchingPieceWhitePawnMoveConditionTest {

    @DisplayName("흰색 폰이 상대말을 잡는 움직임을 확인한다.")
    @Test
    void isSatisfyBy() {
        CatchingPieceWhitePawnMoveCondition condition = new CatchingPieceWhitePawnMoveCondition();
        Board board = new Board(Arrays.asList(
                Piece.createPawn(Color.WHITE, 0, 2),
                Piece.createRook(Color.BLACK, 1, 3)
        ));
        boolean rightActual = condition.isSatisfyBy(board, Piece.createPawn(Color.WHITE, 0, 2),
                new Position(1, 3));
        boolean falseActual = condition.isSatisfyBy(board, Piece.createPawn(Color.WHITE, 0, 2),
                new Position(1, 1));

        assertThat(rightActual).isTrue();
        assertThat(falseActual).isFalse();
    }
}