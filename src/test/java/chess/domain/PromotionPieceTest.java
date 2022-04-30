package chess.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PromotionPieceTest {

    @ParameterizedTest
    @ValueSource(strings = {"q", "r", "p", "k"})
    @DisplayName("Promotion 기물 생성 시 잘못된 입력값이 들어올 경우 예외발생")
    void cretePromotionPieceException(String input) {
        assertThatThrownBy(() -> PromotionPiece.createPromotionPiece(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("불가능한 프로모션 기물 이름입니다.");
    }

    @Test
    @DisplayName("입력값에 따른 기물 생성")
    void createPromotionPiece() {
        PromotionPiece promotionPiece = PromotionPiece.createPromotionPiece("N");
        assertThat(promotionPiece).isEqualTo(PromotionPiece.KNIGHT);
    }
}
