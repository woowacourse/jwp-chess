package chess.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.piece.detail.Team;
import chess.domain.piece.singlemove.King;
import chess.domain.square.Square;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class BlankTest {

    @DisplayName("존재하는 색상으로 빈칸을 생성할 시 예외를 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"WHITE", "BLACK"})
    void cannotCreateBlankWithExistingTeam(final String rawTeam) {
        assertThatThrownBy(() -> new Blank(Team.valueOf(rawTeam), Square.from("a1")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈칸은 팀을 가질 수 없습니다.");
    }

    @DisplayName("빈칸의 이동할 수 있는 위치를 확인할 시, false를 반환한다.")
    @Test
    void blankCannotMove() {
        final Blank blank = new Blank(Team.NONE, Square.from("a1"));
        assertThat(blank.isMovable(new King(Team.BLACK, Square.from("a2")))).isFalse();
    }

    @DisplayName("빈칸이 이동할 수 있는 방향을 찾을 시 빈 리스트를 반환한다.")
    @Test
    void getAvailableDirections() {
        final Blank blank = new Blank(Team.NONE, Square.from("a1"));
        assertThat(blank.getAvailableDirections()).isEqualTo(List.of());
    }

    @DisplayName("빈칸이 움직일 경우 예외를 발생한다.")
    @Test
    void blankCannotMoveToAnywhere() {
        final Piece piece = new Blank(Team.NONE, Square.from("a1"));
        final Square to = Square.from("a2");

        assertThatThrownBy(() -> piece.moveTo(to))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("빈칸은 이동할 수 없습니다.");
    }
}
