package chess.model.square;

import org.junit.jupiter.api.Test;

import java.util.List;

import static chess.model.piece.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class SquareTest {

    @Test
    void findRoad() {
        List<Square> road = D4.findRoad(Direction.NORTH_WEST, 7);

        assertThat(road).containsExactly(C5, B6, A7);
    }

    @Test
    void findLocation() {
        boolean location = D4.findLocation(Direction.SSE, Square.of(File.E, Rank.TWO));

        assertThat(location).isTrue();
    }

    @Test
    void fromString() {
        assertThat(Square.fromString("a4")).isEqualTo(Square.of(File.A, Rank.FOUR));
    }
}
