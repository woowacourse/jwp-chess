package chess.domain.piece;

import static chess.domain.position.File.A;
import static chess.domain.position.File.B;
import static chess.domain.position.File.C;
import static chess.domain.position.File.D;
import static chess.domain.position.File.E;
import static chess.domain.position.File.F;
import static chess.domain.position.File.G;
import static chess.domain.position.Rank.EIGHT;
import static chess.domain.position.Rank.FIVE;
import static chess.domain.position.Rank.ONE;
import static chess.domain.position.Rank.SIX;
import static chess.domain.position.Rank.THREE;
import static chess.domain.position.Rank.TWO;
import static org.assertj.core.api.Assertions.assertThat;
import chess.domain.piece.movementcondition.BaseMovementCondition;
import chess.domain.position.Position;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class BishopTest {

    @ParameterizedTest
    @MethodSource("provideInvalidMoveBishop")
    @DisplayName("비숍은 대각선외에는 움직일 수 없다.")
    void throwExceptionInvalidMoveBishop(Position from, Position to) {
        Bishop bishop = new Bishop(Color.BLACK);

        assertThat(bishop.identifyMovementCondition(from, to))
                .isEqualTo(BaseMovementCondition.IMPOSSIBLE);
    }

    private static Stream<Arguments> provideInvalidMoveBishop() {
        return Stream.of(
                Arguments.of(new Position(C, EIGHT), new Position(D, THREE)),
                Arguments.of(new Position(C, EIGHT), new Position(G, FIVE)),
                Arguments.of(new Position(C, ONE), new Position(B, FIVE)),
                Arguments.of(new Position(C, ONE), new Position(F, SIX))
        );
    }

    @ParameterizedTest
    @MethodSource("provideCrossMoveBishop")
    @DisplayName("비숍은 대각선으로 이동할 수 있다.")
    void moveCrossBishop(Position from, Position to) {
        Bishop bishop = new Bishop(Color.BLACK);

        assertThat(bishop.identifyMovementCondition(from, to))
                .isEqualTo(BaseMovementCondition.MUST_OBSTACLE_FREE);
    }

    private static Stream<Arguments> provideCrossMoveBishop() {
        return Stream.of(
                Arguments.of(new Position(C, EIGHT), new Position(F, FIVE)),
                Arguments.of(new Position(C, EIGHT), new Position(A, SIX)),
                Arguments.of(new Position(C, ONE), new Position(B, TWO)),
                Arguments.of(new Position(C, ONE), new Position(E, THREE))
        );
    }
}
