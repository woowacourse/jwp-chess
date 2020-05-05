package wooteco.chess.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import wooteco.chess.domain.position.Position;

class MoveParameterTest {

    @Test
    void create() {
        Position source = Position.of("A1");
        Position target = Position.of("A2");
        MoveParameter moveParameter = MoveParameter.of(source, target);
        assertThat(moveParameter.getSource()).isEqualTo(source);
        assertThat(moveParameter.getTarget()).isEqualTo(target);
    }

    @Test
    void exception() {
        Position source = Position.of("A1");
        Position target = Position.of("A1");
        assertThatThrownBy(() -> MoveParameter.of(source, target))
            .isInstanceOf(IllegalArgumentException.class);
    }
}