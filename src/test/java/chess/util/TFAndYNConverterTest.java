package chess.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;

class TFAndYNConverterTest {

    @ParameterizedTest
    @CsvSource(value = {"true, Y", "false, N"})
    void convertYN(boolean changer, String expected) {
        assertThat(TFAndYNConverter.convertYN(changer)).isEqualTo(expected);
    }

    @ParameterizedTest
    @NullSource
    void convertTF_WHEN_NULL(String changer) {
        assertThatThrownBy(() -> TFAndYNConverter.convertTF(changer))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("null");
    }

    @ParameterizedTest
    @CsvSource(value = {"Y, true, YY", "N, false, NN"})
    void convertTF(String changer, boolean expected, String failed) {
        assertThat(TFAndYNConverter.convertTF(changer)).isEqualTo(expected);
        assertThatThrownBy(() -> TFAndYNConverter.convertTF(failed))
            .isInstanceOf(IllegalArgumentException.class);
    }
}