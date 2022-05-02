package chess.domain.piece.unit;

import static chess.domain.PositionFixtures.B3;
import static chess.domain.PositionFixtures.B4;
import static chess.domain.PositionFixtures.B5;
import static chess.domain.PositionFixtures.C2;
import static chess.domain.PositionFixtures.C4;
import static chess.domain.PositionFixtures.C6;
import static chess.domain.PositionFixtures.D2;
import static chess.domain.PositionFixtures.D3;
import static chess.domain.PositionFixtures.D4;
import static chess.domain.PositionFixtures.D6;
import static chess.domain.PositionFixtures.E2;
import static chess.domain.PositionFixtures.E4;
import static chess.domain.PositionFixtures.E6;
import static chess.domain.PositionFixtures.F3;
import static chess.domain.PositionFixtures.F4;
import static chess.domain.PositionFixtures.F5;
import static chess.domain.piece.property.Team.WHITE;

import chess.domain.position.Position;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class KnightTest {

    @ParameterizedTest
    @MethodSource("availablePositions")
    @DisplayName("Knight 는 상하좌우로 이동할 수 있다.")
    void moveKnight(Position target) {
        Piece piece = new Knight(WHITE);

        Assertions.assertThat(piece.availableMove(D4, target, true)).isEqualTo(true);
    }

    private static Stream<Position> availablePositions() {
        return Stream.of(B3, B5, C2, C6, E2, E6, F3, F5);
    }

    @ParameterizedTest
    @MethodSource("disablePositions")
    @DisplayName("Knight 는 상하좌우 L자 이외로 이동할 수 없다.")
    void nonMoveKnight(Position target) {
        Piece piece = new Knight(WHITE);

        Assertions.assertThat(piece.availableMove(D4, target, true)).isEqualTo(false);
    }

    private static Stream<Position> disablePositions() {
        return Stream.of(D4, E4, C4, D3, B4, D2, F4, D6);
    }
}
