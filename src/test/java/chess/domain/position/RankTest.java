package chess.domain.position;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class RankTest {

    @ParameterizedTest(name = "RANK : {0}, value : {1}")
    @CsvSource({"ONE,1", "TWO,2", "THREE,3", "FOUR,4", "FIVE,5", "SIX,6", "SEVEN,7", "EIGHT,8"})
    @DisplayName("문자열로 RANK 조회")
    void findRank(String rank, String value) {
        assertThat(Rank.find(value).name()).isEqualTo(rank);
    }

    @Test
    @DisplayName("존재하지 않는 RANK 조회시 예외 발생")
    void findInvalidRank() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> Rank.find("9"))
            .withMessage("존재하지 않는 RANK입니다.");
    }
}
