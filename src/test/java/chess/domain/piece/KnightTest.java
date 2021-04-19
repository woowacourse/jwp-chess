package chess.domain.piece;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

import chess.domain.Position;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class KnightTest {
    @Test
    @DisplayName("Knight이 정상으로 생성되는지 테스트한다.")
    void init() {
        assertThatCode(Knight::new)
                .doesNotThrowAnyException();
    }

    @ParameterizedTest
    @CsvSource(value = {"b5", "b3", "c2", "c6", "e2", "e6", "f3", "f5"})
    @DisplayName("Knight이 규칙상 움직일 수 있는 곳이라면, 참을 반환한다.")
    void when_king_can_move_according_to_rule_return_true(final String dest) {
        final Knight knight = new Knight();
        final Position current = Position.of("d4");
        final Position destination = Position.of(dest);
        final Map<Position, Piece> chessBoard = new HashMap<>();
        chessBoard.put(current, knight);

        assertThat(knight.isMovable(current, destination, chessBoard)).isTrue();
    }
}
