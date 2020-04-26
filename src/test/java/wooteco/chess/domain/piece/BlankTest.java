package wooteco.chess.domain.piece;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BlankTest {

    @Test
    @DisplayName("Blank 생성 테스트")
    void create() {
        assertThat(new Blank()).isInstanceOf(Blank.class);
    }

    @Test
    @DisplayName("이동 여부 테스트")
    void isMovable() {
        Map<Position, Piece> route = new HashMap<>();
        Position start = Position.of(2, 1);
        Position end = Position.of(4, 3);
        route.put(Position.of(2, 1), new Blank());
        route.put(Position.of(3, 2), new Blank());
        route.put(Position.of(4, 3), new Blank());
        Path path = new Path(route, start, end);

        assertThat(new Blank().isMovable(path)).isFalse();
    }

    @Test
    @DisplayName("처음 위치 확인 테스트")
    void isInitialPosition() {
        assertThat(new Blank().isInitialPosition(Position.of(4, 3))).isTrue();
    }

    @Test
    @DisplayName("toString 테스트")
    void string() {
        assertThat(new Blank().toString()).isEqualTo("blank");
    }
}
