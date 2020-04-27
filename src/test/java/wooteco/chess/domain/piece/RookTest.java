package wooteco.chess.domain.piece;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.chess.domain.board.Path;
import wooteco.chess.domain.board.Position;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class RookTest {

    @Test
    @DisplayName("Rook 생성 테스트")
    void create() {
        assertThat(new Rook(Team.WHITE)).isInstanceOf(Rook.class);
    }

    @Test
    @DisplayName("이동 여부 테스트")
    void isMovable() {
        Map<Position, Piece> route = new HashMap<>();
        Position start = Position.of(2, 1);
        Position end = Position.of(4, 1);
        route.put(Position.of(2, 1), new Rook(Team.WHITE));
        route.put(Position.of(3, 1), new Blank());
        route.put(Position.of(4, 1), new Blank());
        Path path = new Path(route, start, end);

        assertThat(new Rook(Team.WHITE).isMovable(path)).isTrue();
    }

    @Test
    @DisplayName("처음 위치 확인 테스트")
    void isInitialPosition() {
        assertThat(new Rook(Team.WHITE).isInitialPosition(Position.of(1, 1))).isTrue();
    }
}
