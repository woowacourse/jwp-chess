package chess.domain.room;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PasswordTest {

    @ParameterizedTest
    @DisplayName("값이 공백이면 예외를 던진다.")
    @ValueSource(strings = {"", "  "})
    void validate(final String value) {
        // then
        assertThatThrownBy(() -> new Password(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호에 공백은 허용되지 않습니다.");
    }

    @ParameterizedTest
    @DisplayName("암호화된 비밀번호와 같은 비밀번호인지 확인한다.")
    @CsvSource(value = {"1234:true", "4321:false"}, delimiter = ':')
    void isSame(final String plainPassword, final boolean expected) {
        // given
        final Password password = new Password("1234");

        // when
        final boolean actual = password.isSame(plainPassword);

        // then
        assertThat(actual).isEqualTo(expected);
    }
}