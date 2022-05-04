package chess.domain.room;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class RoomNameTest {

    @ParameterizedTest
    @DisplayName("값이 공백이면 예외를 던진다.")
    @ValueSource(strings = {"", "  "})
    void validate_blank(final String value) {
        // then
        assertThatThrownBy(() -> new RoomName(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("방 이름에 공백은 허용되지 않습니다.");
    }

    @Test
    @DisplayName("최대 길이를 넘으면 예외를 던진다.")
    void validate_max_length() {
        // given
        final String value = "12345678901";

        // then
        assertThatThrownBy(() -> new RoomName(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("방 이름은 최대 10자 까지 허용됩니다.");
    }
}