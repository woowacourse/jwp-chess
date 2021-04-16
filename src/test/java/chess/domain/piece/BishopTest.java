package chess.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.position.Position;
import chess.domain.position.Target;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BishopTest {

    private final Position source = new Position("e", "4");
    private final Piece white = new Bishop(Color.WHITE, source);

    @ParameterizedTest
    @DisplayName("이동 가능한지 판단하는 기능")
    @ValueSource(strings = {"d5", "f5", "d3", "f3"})
    void canMove(final String input) {
        final Position position = new Position(input);
        final Target target = new Target(new Queen(Color.BLACK, position));
        assertThat(white.canMove(target)).isTrue();
        assertThat(white.canMove(new Target(new Blank(position)))).isTrue();
    }
}