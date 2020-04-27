package chess.model.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.model.domain.board.Square;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class KnightTest {

    @Test
    @DisplayName("Null이 of에 들어갔을 때 예외 발생")
    void validNotNull() {
        assertThatThrownBy(() -> Knight.getInstance(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Null");
    }

    @ParameterizedTest
    @ValueSource(strings = {"b1", "a2", "b5", "a4", "e4", "d1", "d5", "e2"})
    @DisplayName("말의 위치(knight)를 받고 말의 종류에 따라 이동할 수 있는 칸 리스트 반환")
    void calculateScopeKnight(String input) {
        Piece piece = Knight.getInstance(Team.BLACK);
        Set<Square> availableSquares = piece.getAllMovableArea(Square.of("c3"));
        assertThat(availableSquares.contains(Square.of(input))).isTrue();
        assertThat(availableSquares.size()).isEqualTo(8);
    }

    @ParameterizedTest
    @ValueSource(strings = {"c4", "d5", "d1", "f1", "f5", "g2"})
    @DisplayName("판의 정보를 가져와서 나이트가 갈 수 있는 칸에 장애물이 있는지 판단하여 이동할 수 있는 리스트 반환하는 테스트")
    void movableKnightSquareTest(String input) {
        Map<Square, Piece> board = new HashMap<>();
        board.put(Square.of("d5"), King.getInstance(Team.BLACK));
        board.put(Square.of("c2"), Queen.getInstance(Team.WHITE));
        board.put(Square.of("g4"), Pawn.getInstance(Team.WHITE));
        Piece piece = Knight.getInstance(Team.WHITE);
        Set<Square> availableSquares = piece.findMovableAreas(Square.of("e3"), board);
        assertThat(availableSquares.contains(Square.of(input))).isTrue();
        assertThat(availableSquares.size()).isEqualTo(6);
    }

    @Test
    @DisplayName("두 동일한 객체를 가져왔을 때 같은지 확인")
    void checkSameInstance() {
        Piece piece = Knight.getInstance(Team.BLACK);
        assertThat(piece).isEqualTo(Knight.getInstance(Team.BLACK));
    }

    @Test
    @DisplayName("체스 말이 같은 색인지 검증하는 테스트")
    void isBlack() {
        assertThat(Knight.getInstance(Team.BLACK).isSameTeam(Team.BLACK)).isTrue();
        assertThat(Knight.getInstance(Team.BLACK).isSameTeam(Team.WHITE)).isFalse();
        assertThat(Knight.getInstance(Team.WHITE).isSameTeam(Team.BLACK)).isFalse();
        assertThat(Knight.getInstance(Team.WHITE).isSameTeam(Team.WHITE)).isTrue();
    }


}
