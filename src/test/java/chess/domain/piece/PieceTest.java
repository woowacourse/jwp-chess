package chess.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PieceTest {
    @Test
    @DisplayName("색에 따라 이름이 정해지는지 확인")
    void create() {
        final Piece piece = new King(Team.BLACK);
        assertThat(piece.team()).isEqualTo(Team.BLACK);
        assertThat(piece.name()).isEqualTo("K");
    }
}