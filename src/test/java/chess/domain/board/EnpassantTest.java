package chess.domain.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.state.MoveInfo;
import org.junit.jupiter.api.Test;

class EnpassantTest {

    @Test
    void getBetweenWhenJumpRank() {
        assertThatThrownBy(() -> EnPassant.getBetween(new MoveInfo("a1", "a2")))
            .isInstanceOf(IllegalArgumentException.class);
        assertThat(EnPassant.getBetween(new MoveInfo("a1", "a3")))
            .isEqualTo(Square.of("a2"));
    }
}