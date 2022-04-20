package chess.domain.game.state;

import static chess.domain.ChessFixtures.A2;
import static chess.domain.ChessFixtures.A4;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

public class StartTest {
    
    @Test
    void start() {
        assertThat(new Start().start()).isInstanceOf(WhiteTurn.class);
    }

    @Test
    void end() {
        assertThatThrownBy(() -> new Start().end())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void move() {
        assertThatThrownBy(() -> new Start().move(A2, A4))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void is_finished() {
        assertThat(new Start().isFinished()).isFalse();
    }
}
