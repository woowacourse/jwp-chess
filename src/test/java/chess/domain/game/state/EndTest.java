package chess.domain.game.state;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.board.Board;
import chess.domain.board.coordinate.Coordinate;
import org.junit.jupiter.api.Test;

class EndTest {

    @Test
    void start() {
        assertThatThrownBy(() -> new End(Board.create()).start())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void end() {
        assertThatThrownBy(() -> new End(Board.create()).end())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void move() {
        assertThatThrownBy(() -> new End(Board.create()).move(Coordinate.of("a2"), Coordinate.of("a4")))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void isFinished() {
        assertThat(new End(Board.create()).isFinished()).isTrue();
    }
}
