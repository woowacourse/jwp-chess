package chess.domain.piece;

import chess.domain.position.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PieceTest {
    @Test
    @DisplayName("색에 따라 이름이 정해지는지 확인")
    void create() {
        final Piece piece = new King(Color.BLACK, new Position("2", "a"));
        assertThat(piece.team()).isEqualTo(Color.BLACK);
        assertThat(piece.name()).isEqualTo("K");
    }

    @Test
    void position() {
        final Pawn pawn = new Pawn(Color.BLACK, new Position("2", "a"));
        assertThat(pawn.position()).isEqualTo("2a");
    }
}