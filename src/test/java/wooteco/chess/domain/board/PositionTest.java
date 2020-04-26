package wooteco.chess.domain.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PositionTest {

    @Test
    @DisplayName("Position 생성 테스트")
    void create() {
        assertThat(Position.of(2, 1)).isInstanceOf(Position.class);
    }

    @Test
    @DisplayName("Position 생성 테스트 - 잘못된 좌표일 경우")
    void create_IfWrongPosition_ThrowException() {
        assertThatThrownBy(() -> Position.of("2,1")).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잘못된 좌표 값입니다.");
    }

    @Test
    @DisplayName("두 점 사이 존재 테스트")
    void inBetween() {
        Position position = Position.of(2, 2);
        Position start = Position.of(2, 1);
        Position end = Position.of(2, 3);
        assertThat(position.inBetween(start, end)).isTrue();
    }

    @Test
    @DisplayName("같은 x 좌표 테스트")
    void isOnX() {
        Position position = Position.of(2, 2);
        assertThat(position.isOnX(2)).isTrue();
    }

    @Test
    @DisplayName("같은 y 좌표 테스트")
    void isOnY() {
        Position position = Position.of(2, 2);
        assertThat(position.isOnY(Coordinate.of(2))).isTrue();
    }

    @Test
    @DisplayName("x좌표 거리 테스트")
    void xDistance() {
        Position start = Position.of(2, 2);
        Position end = Position.of(4, 4);
        assertThat(start.xDistance(end)).isEqualTo(2);
    }

    @Test
    @DisplayName("y좌표 거리 테스트")
    void yDistance() {
        Position start = Position.of(2, 2);
        Position end = Position.of(4, 4);
        assertThat(start.yDistance(end)).isEqualTo(2);
    }

    @Test
    @DisplayName("x좌표 크기 비교 테스트")
    void hasLargerX() {
        Position start = Position.of(2, 2);
        Position end = Position.of(4, 4);
        assertThat(start.hasLargerX(end)).isFalse();
    }
}
