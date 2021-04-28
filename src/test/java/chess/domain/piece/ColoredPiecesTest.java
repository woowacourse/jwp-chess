package chess.domain.piece;

import chess.exception.DomainException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ColoredPiecesTest {
    ColoredPieces coloredPieces;

    @BeforeEach
    void setUp() {
        coloredPieces = ColoredPieces.createByColor(Color.BLACK);
    }

    @DisplayName("킹이 살아있는지 확인한다")
    @Test
    void isKingAliveTest() {
        assertThat(coloredPieces.isKingAlive()).isTrue();
    }

    @DisplayName("다른 진영의 말을 제거하려하면 예외를 발생시킨다.")
    @Test
    void throwExceptionWhenOtherColorRemove() {
        assertThatThrownBy(() -> coloredPieces.remove(new Rook(Color.WHITE)))
                .isInstanceOf(DomainException.class)
                .hasMessage("진영이 다른 피스를 제거할 수 없습니다");
    }
}