package springchess.model.square;

import chess.model.square.Direction;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DirectionTest {

    @Test
    void getDistanceFrom() {
        List<Integer> coordinates = Direction.NORTH_WEST.getDistanceFrom(3);

        assertThat(coordinates).isEqualTo(List.of(-3, 3));
    }
}
