package chess.domain.game.state;

import static chess.domain.ChessFixtures.A2;
import static chess.domain.ChessFixtures.A4;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.board.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EndTest {

    private Board board;

    @BeforeEach
    void setupBoard() {
        board = Board.create();
    }

    @Test
    void start() {
        assertThatThrownBy(() -> new End(board).start())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void end() {
        assertThatThrownBy(() -> new End(board).end())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void move() {
        assertThatThrownBy(() -> new End(board).move(A2, A4))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void isFinished() {
        assertThat(new End(board).isFinished()).isTrue();
    }
}
