package wooteco.chess.domain.position;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class PositionTest {
    @ParameterizedTest
    @ValueSource(strings = {"a1", "h8", "a8", "h1"})
    void create(String input) {
        assertThat(Position.of(input)).isEqualTo(Position.of(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a9", "h9", "a0", "h0", "i1", "i8"})
    void wrongPosition(String input) {
        assertThatThrownBy(() -> Position.of(input))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void fileDifferenceFromTargetPosition() {
        Position A1 = Position.of("A1");
    }

    @Test
    void getFileDifference() {
        assertThat(Position.of("A1").getFileDifference(Position.of("D1"))).isEqualTo(3);
    }

    @Test
    void getRankDifference() {
        assertThat(Position.of("A1").getRankDifference(Position.of("A5"))).isEqualTo(4);
    }

    @ParameterizedTest
    @ValueSource(strings = {"A1", "A2", "A3", "A4", "A3", "A5", "A7"})
    void getName(String input) {
        Position position = Position.of(input);
        assertThat(position.getName()).isEqualTo(input);
    }
}
