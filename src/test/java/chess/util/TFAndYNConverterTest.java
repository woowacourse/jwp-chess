package chess.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class TFAndYNConverterTest {

    @ParameterizedTest
    @CsvSource(value = {"true, Y", "false, N"})
    void convertYN(boolean changer, String expected) {
        assertThat(TFAndYNConverter.convertYN(changer)).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource(value = {"Y, true", "N, false"})
    void convertTF(String changer, boolean expected) {
        assertThat(TFAndYNConverter.convertTF(changer)).isEqualTo(expected);
    }
}