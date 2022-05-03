package chess.domain.position;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class FileTest {

    @ParameterizedTest(name = "FILE : {0}, value : {1}")
    @CsvSource({"A,1", "B,2", "C,3", "D,4", "E,5", "F,6", "G,7", "H,8"})
    @DisplayName("문자열로 FILE 조회")
    void findRank(String rank, String value) {
        assertThat(File.find(value).name()).isEqualTo(rank);
    }

    @Test
    @DisplayName("존재하지 않는 FILE 조회시 예외 발생")
    void findInvalidRank() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> File.find("9"))
            .withMessage("존재하지 않는 FILE입니다.");
    }
}
