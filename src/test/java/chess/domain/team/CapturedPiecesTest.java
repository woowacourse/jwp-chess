package chess.domain.team;

import chess.domain.piece.Queen;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;

public class CapturedPiecesTest {
    @Test
    @DisplayName("기물을 잡는 것이 정상 작동한다.")
    void add_test() {
        final CapturedPieces capturedPieces = new CapturedPieces();
        assertThatCode(() -> capturedPieces.add(new Queen()))
                .doesNotThrowAnyException();
    }
}