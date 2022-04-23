package chess.domain.piece.unit;

import static chess.domain.piece.property.Team.*;

import chess.domain.PositionFixtures;
import chess.domain.position.Position;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class RookTest {

    @ParameterizedTest
    @MethodSource("availablePositions")
    @DisplayName("Rook 은 상하좌우로 이동할 수 있다.")
    void moveRookUpDownRightLeft(Position target) {
        Piece piece = new Rook(WHITE);

        Assertions.assertThat(piece.availableMove(PositionFixtures.B2, target, true)).isEqualTo(true);
    }

    private static Stream<Position> availablePositions() {
        return Stream.of(PositionFixtures.A2, PositionFixtures.B1, PositionFixtures.B8, PositionFixtures.H2);
    }
}
