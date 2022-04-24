package chess.domain.piece;

import static chess.domain.position.File.C;
import static chess.domain.position.File.D;
import static chess.domain.position.File.E;
import static chess.domain.position.File.F;
import static chess.domain.position.File.G;
import static chess.domain.position.Rank.EIGHT;
import static chess.domain.position.Rank.FIVE;
import static chess.domain.position.Rank.FOUR;
import static chess.domain.position.Rank.SEVEN;
import static chess.domain.position.Rank.SIX;
import static chess.domain.position.Rank.THREE;
import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.piece.movementcondition.BaseMovementCondition;
import chess.domain.position.Position;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class KnightTest {

    @Test
    @DisplayName("나이트가 이동할 수 없는 위치로 이동 시 예외 발생")
    void moveKnightToInvalidPosition() {
        Knight knight = new Knight(Color.BLACK);

        assertThat(knight.identifyMovementCondition(new Position(G, EIGHT), new Position(F, FIVE)))
                .isEqualTo(BaseMovementCondition.IMPOSSIBLE);
    }

    @ParameterizedTest
    @MethodSource("provideValidMoveKnight")
    @DisplayName("나이트는 직선으로 1칸 이동 후 대각선으로 1칸 움직인다.")
    void moveKnightToValidPosition(Position from, Position to) {
        Knight knight = new Knight(Color.BLACK);

        assertThat(knight.identifyMovementCondition(from, to))
                .isEqualTo(BaseMovementCondition.POSSIBLE);
    }

    private static Stream<Arguments> provideValidMoveKnight() {
        return Stream.of(
                Arguments.of(new Position(E, FIVE), new Position(G, FOUR)),
                Arguments.of(new Position(E, FIVE), new Position(F, THREE)),
                Arguments.of(new Position(E, FIVE), new Position(D, THREE)),
                Arguments.of(new Position(E, FIVE), new Position(C, FOUR)),
                Arguments.of(new Position(E, FIVE), new Position(C, SIX)),
                Arguments.of(new Position(E, FIVE), new Position(D, SEVEN)),
                Arguments.of(new Position(E, FIVE), new Position(F, SEVEN)),
                Arguments.of(new Position(E, FIVE), new Position(G, SIX))
        );
    }
}
