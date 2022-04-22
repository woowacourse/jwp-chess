package chess.domain.square;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import chess.domain.piece.detail.Direction;

class SquareTest {

    @DisplayName("rawSquare로 생성한 스퀘어가 파일, 랭크로 생성한 스퀘어와 동등하다.")
    @ParameterizedTest
    @CsvSource({"a1,A,ONE", "h8,H,EIGHT"})
    void from(final String rawSquare, final String rawFile, final String rawRank) {
        Square square1 = Square.from(rawSquare);
        Square square2 = new Square(File.valueOf(rawFile), Rank.valueOf(rawRank));

        assertThat(square1).isEqualTo(square2);
    }

    @DisplayName("길이가 2가 아닌 문자열로 스퀘어를 생성하려 할 시 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"sun", "s"})
    void invalidLengthException(final String rawSquare) {
        assertThatThrownBy(() -> Square.from(rawSquare))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("스퀘어의 길이가 올바르지 않습니다.");
    }

    @DisplayName("존재하지 않는 스퀘어로 스퀘어를 찾을 시 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"a0", "z1", "h9", "i8"})
    void notExistException(final String rawSquare) {
        assertThatThrownBy(() -> Square.from(rawSquare))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("존재하지 않는 스퀘어입니다.");
    }

    @DisplayName("방향을 받아서 그 방향만큼 이동한 위치를 반환한다.")
    @Test
    void next() {
        final Square from = Square.from("a1");
        final Square to = Square.from("b1");

        assertThat(from.next(Direction.E)).isEqualTo(to);
    }
}
