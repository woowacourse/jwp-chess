package chess.domain;

import static chess.domain.ChessFixtures.A1;
import static chess.domain.ChessFixtures.A2;
import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.coordinate.Column;
import chess.domain.board.coordinate.Coordinate;
import chess.domain.board.coordinate.Row;
import chess.domain.direction.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CoordinateTest {

    @Test
    @DisplayName("정적 팩터리 메소드로 캐시에서 올바르게 객체를 가져오는지 확인")
    void create() {
        Coordinate coordinate = Coordinate.of(Column.A, Row.ONE);
        assertThat(coordinate).isEqualTo(A1);
    }

    @Test
    @DisplayName("방향이 주어지면 해당 방향으로 이동한 좌표를 반환한다.")
    void next() {
        Coordinate coordinate = Coordinate.of(Column.A, Row.ONE);
        Coordinate nextCoordinate = coordinate.next(Direction.UP);

        assertThat(nextCoordinate).isEqualTo(A2);
    }
}
