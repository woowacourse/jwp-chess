package chess.domain.piece;

import chess.domain.position.Position;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PieceTest {
    @Test
    @DisplayName("색에 따라 이름이 정해지는지 확인")
    void create() {
        final Piece piece = new King(Color.BLACK, new Position("2", "a"));
        assertThat(piece.team()).isEqualTo(Color.BLACK);
        assertThat(piece.name()).isEqualTo("K");
    }

    @Test
    @DisplayName("정상 위치 생성 확인")
    void position() {
        final Pawn pawn = new Pawn(Color.BLACK, new Position("2", "a"));
        assertThat(pawn.position()).isEqualTo("2a");
    }

    @Test
    @DisplayName("없는 위치로 객체를 생성하려 하면 에러 발생")
    void positionException() {
        assertThatThrownBy( () ->  new Pawn(Color.BLACK, new Position("9", "a")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당하는 Rank 위치를 찾을 수 없습니다.");

        assertThatThrownBy( () ->  new Pawn(Color.BLACK, new Position("1", "z")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당하는 File 위치를 찾을 수 없습니다.");
    }
}