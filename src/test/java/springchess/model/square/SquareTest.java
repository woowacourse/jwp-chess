package springchess.model.square;

import chess.model.square.Direction;
import chess.model.square.File;
import chess.model.square.Rank;
import chess.model.square.Square;
import org.junit.jupiter.api.Test;

import java.util.List;

import static chess.model.piece.Fixtures.*;
import static org.assertj.core.api.Assertions.assertThat;

class SquareTest {

    @Test
    void findRoad() {
        List<chess.model.square.Square> road = D4.findRoad(chess.model.square.Direction.NORTH_WEST, 7);

        assertThat(road).containsExactly(C5, B6, A7);
    }

    @Test
    void findLocation() {
        boolean location = D4.findLocation(Direction.SSE, chess.model.square.Square.of(chess.model.square.File.E, chess.model.square.Rank.TWO));

        assertThat(location).isTrue();
    }

    @Test
    void fromString() {
        assertThat(chess.model.square.Square.fromString("a4")).isEqualTo(Square.of(File.A, Rank.FOUR));
    }
}
