package chess.domain.square;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FileTest {

    @DisplayName("파일 이름으로 자신의 알파벳을 반환한다.")
    @ParameterizedTest
    @CsvSource({"A,a", "B,b", "C,c", "D,d", "E,e", "F,f", "G,g", "H,h"})
    void getRank(final String name, final char value) {
        File enumName = File.valueOf(name);
        assertThat(enumName.getValue()).isEqualTo(value);
    }

    @DisplayName("숫자로 자신의 파일를 반환한다.")
    @ParameterizedTest
    @CsvSource({"A,a", "B,b", "C,c", "D,d", "E,e", "F,f", "G,g", "H,h"})
    void from(final String name, final char value) {
        File file = File.from(value);
        File enumName = File.valueOf(name);
        assertThat(file).isSameAs(enumName);
    }

    @DisplayName("허용되지 않은 알파벳으로 파일을 생성할 시 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"i", "z"})
    void invalidRankException(final char value) {
        assertThatThrownBy(() -> File.from(value))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(value + "는 존재하지 않는 파일입니다.");
    }

    @DisplayName("들어온 값에 따라 변경된 파일을 반환한다.")
    @Test
    void add() {
        assertThat(File.A.add(1)).isEqualTo(File.B);
    }
}
