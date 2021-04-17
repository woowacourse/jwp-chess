package chess.domain.position;

import static chess.domain.position.type.File.A;
import static chess.domain.position.type.Rank.THREE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PositionsTest {

    @DisplayName("유효한 위치")
    @Test
    void validPosition() {
        Position position = Position.of("a3");

        assertThat(position).isEqualTo(new Position(A, THREE));
    }


    @DisplayName("유효하지 않은 위치")
    @Test
    void invalidPosition() {
        assertThatThrownBy(() -> Position.of("a0"))
            .isInstanceOf(IllegalArgumentException.class);
    }
}