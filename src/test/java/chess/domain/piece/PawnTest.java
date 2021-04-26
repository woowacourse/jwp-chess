package chess.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.position.Position;
import chess.domain.position.Target;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PawnTest {

    @ParameterizedTest
    @DisplayName("앞으로 한칸 이동 가능한 지 판단하는 기능")
    @ValueSource(strings = {"a2,a3,WHITE"})
    void canMoveOneBlock(final String input) {
        final String[] inputs = input.split(",");
        final Position source = new Position(inputs[0]);
        final Position target = new Position(inputs[1]);
        final Color color = Color.valueOf(inputs[2]);

        final Pawn white = new Pawn(color, source);
        assertThat(white.canMove(new Target(new Blank(target))))
            .isTrue();
        assertThat(white.canMove(new Target(new King(Color.WHITE, target))))
            .isFalse();
    }

    @ParameterizedTest
    @DisplayName("대각선 방향으로 한 칸 이동 가능한 지 판단하는 기능")
    @ValueSource(strings = {"a2,b3,WHITE"})
    void canMoveBlack(final String input) {
        final String[] inputs = input.split(",");
        final Position source = new Position(inputs[0]);
        final Position target = new Position(inputs[1]);
        final Color color = Color.valueOf(inputs[2]);

        final Pawn white = new Pawn(color, source);
        assertThat(white.canMove(new Target(new Pawn(Color.BLACK, target))))
            .isTrue();
        assertThat(white.canMove(new Target(new Pawn(color, target))))
            .isFalse();
    }

    @ParameterizedTest
    @DisplayName("처음 움직이는 경우 두칸 이동 가능 판단 기능")
    @ValueSource(strings = {"a2,a4,WHITE", "a7,a5,BLACK"})
    void checkInitialMove(final String input) {
        final String[] inputs = input.split(",");
        final Position source = new Position(inputs[0]);
        final Position target = new Position(inputs[1]);
        final Color color = Color.valueOf(inputs[2]);
        final Pawn white = new Pawn(color, source);

        assertThat(white.canMove(new Target(new Blank(target))))
            .isTrue();
    }
}