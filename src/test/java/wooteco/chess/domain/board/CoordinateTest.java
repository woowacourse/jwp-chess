package wooteco.chess.domain.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CoordinateTest {

    @Test
    @DisplayName("Coordinate 생성 테스트")
    void create() {
        assertThat(Coordinate.of(3)).isInstanceOf(Coordinate.class);
    }

    @Test
    @DisplayName("다른 좌표와의 거리 테스트")
    void distance() {
        Coordinate coordinateA = Coordinate.of(3);
        Coordinate coordinateB = Coordinate.of(8);
        assertThat(coordinateA.distance(coordinateB)).isEqualTo(5);
    }

    @Test
    @DisplayName("다른 좌표와 크기 비교 테스트")
    void isLargerThan() {
        Coordinate coordinateA = Coordinate.of(3);
        Coordinate coordinateB = Coordinate.of(8);
        assertThat(coordinateA.isLargerThan(coordinateB)).isFalse();
    }

    @Test
    @DisplayName("두 좌표 사이 존재 테스트")
    void isMiddle() {
        Coordinate start = Coordinate.of(3);
        Coordinate end = Coordinate.of(8);
        Coordinate checkCoordinate = Coordinate.of(5);
        assertThat(checkCoordinate.isMiddle(start, end)).isTrue();
    }

    @Test
    @DisplayName("좌표에 해당하는 알파벳 추출 테스트")
    void getAlphabet() {
        Coordinate coordinate = Coordinate.of(8);
        assertThat(coordinate.getAlphabet()).isEqualTo("h");
    }

    @Test
    @DisplayName("toString 테스트")
    void string() {
        Coordinate coordinate = Coordinate.of(8);
        assertThat(coordinate.toString()).isEqualTo("8");
    }
}
