package chess.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PositionTest {

    @DisplayName("포지션 생성 테스트")
    @Test
    void createPosition() {
        Position position = new Position(0, 0);

        assertThat(position).isEqualTo(new Position(0, 0));
    }

}