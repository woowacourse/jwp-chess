package chess.domain.game.room;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RoomPasswordTest {
    @DisplayName("빈 패스워드를 전달하면 예외가 발생한다.")
    @Test
    void validateNotEmpty() {
        assertThatThrownBy(() -> RoomPassword.createByPlainText(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 패스워드로 설정할 수 없습니다.");
    }
}