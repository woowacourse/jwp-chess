package chess.chessgame.domain.room.game.board;

import chess.chessgame.domain.room.game.board.order.MoveOrder;
import chess.chessgame.domain.room.game.board.position.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static chess.chessgame.domain.room.game.board.piece.Fixture.mockBoard;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SquareTest {
    @DisplayName("move시에 말이 없으면 예외")
    @Test
    void throwExceptionWhenEmptySquare() {
        Square c3 = mockBoard.findByPosition(Position.of("c3"));
        MoveOrder moveOrder = mockBoard.createMoveOrder(Position.of("c3"), Position.of("c4"));

        assertThatThrownBy(() -> c3.move(moveOrder))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 위치엔 말이 없습니다.");
    }
}