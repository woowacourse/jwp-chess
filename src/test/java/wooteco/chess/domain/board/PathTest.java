package wooteco.chess.domain.board;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.chess.domain.piece.Blank;
import wooteco.chess.domain.piece.Pawn;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Team;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PathTest {

    @Test
    @DisplayName("Path 생성 테스트")
    void create() {
        Map<Position, Piece> route = new HashMap<>();
        Position start = Position.of(2, 1);
        Position end = Position.of(4, 1);

        route.put(Position.of(2, 1), new Pawn(Team.WHITE));
        route.put(Position.of(3, 1), new Blank());
        route.put(Position.of(4, 1), new Blank());

        assertThat(new Path(route, start, end)).isInstanceOf(Path.class);
    }

    @Test
    @DisplayName("Path 생성 테스트 - 시작점을 포함하지 않을 경우")
    void create_IfNotContainInitialPoint_ThrowException() {
        Map<Position, Piece> route = new HashMap<>();
        Position start = Position.of(2, 1);
        Position end = Position.of(4, 1);

        route.put(Position.of(3, 1), new Blank());
        route.put(Position.of(4, 1), new Blank());

        assertThatThrownBy(() -> new Path(route, start, end)).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("시작점을 포함하지 않습니다.");
    }

    @Test
    @DisplayName("제곱 거리 합 테스트")
    void distanceSquare() {
        Map<Position, Piece> route = new HashMap<>();
        Position start = Position.of(2, 1);
        Position end = Position.of(4, 1);

        route.put(Position.of(2, 1), new Pawn(Team.WHITE));
        route.put(Position.of(3, 1), new Blank());
        route.put(Position.of(4, 1), new Blank());

        assertThat(new Path(route, start, end).distanceSquare()).isEqualTo(4);
    }

    @Test
    @DisplayName("도착점 빈 공간 확인 테스트")
    void isEndEmpty() {
        Map<Position, Piece> route = new HashMap<>();
        Position start = Position.of(2, 1);
        Position end = Position.of(4, 1);

        route.put(Position.of(2, 1), new Pawn(Team.WHITE));
        route.put(Position.of(3, 1), new Blank());
        route.put(Position.of(4, 1), new Blank());

        assertThat(new Path(route, start, end).isEndEmpty()).isTrue();
    }

    @Test
    @DisplayName("도착점 상대 말 확인 테스트")
    void isEnemyOnEnd() {
        Map<Position, Piece> route = new HashMap<>();
        Position start = Position.of(2, 1);
        Position end = Position.of(4, 1);

        route.put(Position.of(2, 1), new Pawn(Team.WHITE));
        route.put(Position.of(3, 1), new Blank());
        route.put(Position.of(4, 1), new Blank());

        assertThat(new Path(route, start, end).isEnemyOnEnd()).isFalse();
    }

    @Test
    @DisplayName("이동 경로 막힘 테스트")
    void isBlocked() {
        Map<Position, Piece> route = new HashMap<>();
        Position start = Position.of(2, 1);
        Position end = Position.of(4, 1);

        route.put(Position.of(2, 1), new Pawn(Team.WHITE));
        route.put(Position.of(3, 1), new Blank());
        route.put(Position.of(4, 1), new Blank());

        assertThat(new Path(route, start, end).isBlocked()).isFalse();
    }

    @Test
    @DisplayName("이동 경로 직진 테스트")
    void isStraight() {
        Map<Position, Piece> route = new HashMap<>();
        Position start = Position.of(2, 1);
        Position end = Position.of(4, 1);

        route.put(Position.of(2, 1), new Pawn(Team.WHITE));
        route.put(Position.of(3, 1), new Blank());
        route.put(Position.of(4, 1), new Blank());

        assertThat(new Path(route, start, end).isStraight()).isTrue();
    }

    @Test
    @DisplayName("이동 경로 대각선 테스트")
    void isDiagonal() {
        Map<Position, Piece> route = new HashMap<>();
        Position start = Position.of(2, 1);
        Position end = Position.of(4, 1);

        route.put(Position.of(2, 1), new Pawn(Team.WHITE));
        route.put(Position.of(3, 1), new Blank());
        route.put(Position.of(4, 1), new Blank());

        assertThat(new Path(route, start, end).isDiagonal()).isFalse();
    }

    @Test
    @DisplayName("처음 이동 여부 테스트")
    void isOnInitialPosition() {
        Map<Position, Piece> route = new HashMap<>();
        Position start = Position.of(2, 1);
        Position end = Position.of(4, 1);

        route.put(Position.of(2, 1), new Pawn(Team.WHITE));
        route.put(Position.of(3, 1), new Blank());
        route.put(Position.of(4, 1), new Blank());

        assertThat(new Path(route, start, end).isOnInitialPosition()).isTrue();
    }

    @Test
    @DisplayName("정면을 바라보는지 여부 테스트")
    void headingForward() {
        Map<Position, Piece> route = new HashMap<>();
        Position start = Position.of(2, 1);
        Position end = Position.of(4, 1);

        route.put(Position.of(2, 1), new Pawn(Team.WHITE));
        route.put(Position.of(3, 1), new Blank());
        route.put(Position.of(4, 1), new Blank());

        assertThat(new Path(route, start, end).headingForward()).isTrue();
    }
}
