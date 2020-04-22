package chess.model.domain.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class SquareTest {

    @Test
    @DisplayName("칸 캐싱했는지 확인")
    void checkSquareCache() {
        Assertions.assertThat(Square.of("a1")).isEqualTo(Square.of("a1"));
    }

    @DisplayName("Null이 of에 들어갔을 때 예외 발생")
    @Test
    void validNotNull() {
        assertThatThrownBy(() -> Square.of(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Null");
        assertThatThrownBy(() -> Square.of(null, null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Null");
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"a9", "f0", "jkl", "j3"})
    @DisplayName("잘못된 값이 of에 들어갔을 때 예외 발생")
    void validLocation(String location) {
        assertThatThrownBy(() -> Square.of(location))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("더한 값이 캐싱된 값이면 캐싱 값 리턴, 아니면 본인 리턴")
    @Test
    void canAddTest() {
        Square square = Square.of("a2");
        assertThat(square.getIncreased(1, 1)).isEqualTo(Square.of("b3"));
    }

    @DisplayName("같은 File의 Square인지 확인")
    @Test
    void isSameFile() {
        Square squareA2 = Square.of("a2");
        Square squareA3 = Square.of("a3");
        Square squareB2 = Square.of("b2");
        assertThat(squareA2.isSameFile(squareA3)).isTrue();
        assertThat(squareA2.isSameFile(squareB2)).isFalse();
    }

    @DisplayName("File의 차이를 가져옴")
    @Test
    void getFileSubtract() {
        Square squareA3 = Square.of("a3");
        Square squareB2 = Square.of("b2");
        Square squareC1 = Square.of("c1");
        assertThat(squareB2.getFileCompare(squareA3)).isEqualTo(1);
        assertThat(squareB2.getFileCompare(squareB2)).isEqualTo(0);
        assertThat(squareB2.getFileCompare(squareC1)).isEqualTo(-1);
    }

    @DisplayName("Rank의 차이를 가져옴")
    @Test
    void getRankSubtract() {
        Square squareA3 = Square.of("a3");
        Square squareB2 = Square.of("b2");
        Square squareC1 = Square.of("c1");
        assertThat(squareB2.getRankCompare(squareA3)).isEqualTo(-1);
        assertThat(squareB2.getRankCompare(squareB2)).isEqualTo(0);
        assertThat(squareB2.getRankCompare(squareC1)).isEqualTo(1);
    }

}
