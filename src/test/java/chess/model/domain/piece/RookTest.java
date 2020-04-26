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

public class RookTest {

    @Test
    @DisplayName("Null이 of에 들어갔을 때 예외 발생")
    void validNotNull() {
        assertThatThrownBy(() -> Rook.getInstance(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Null");
    }

    @ParameterizedTest
    @ValueSource(strings = {"d1", "d3", "d4", "d5", "d6", "d7", "d8", "a2", "b2", "c2", "e2", "f2",
        "g2", "h2"})
    @DisplayName("말의 위치(룩)를 받고 말의 종류에 따라 이동할 수 있는 칸 리스트 반환")
    void calculateScopeRook(String input) {
        Piece piece = Rook.getInstance(Team.WHITE);
        Set<Square> availableSquares = piece.getAllMovableArea(Square.of("d2"));
        assertThat(availableSquares.contains(Square.of(input))).isTrue();
        assertThat(availableSquares.size()).isEqualTo(14);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a6", "b6", "d6", "e6", "c7"})
    @DisplayName("판의 정보를 가져와서 rook이 갈 수 있는 칸에 장애물이 있는지 판단하여 이동할 수 있는 리스트 반환하는 테스트")
    void movableRookSquareTest(String input) {
        Map<Square, Piece> board = new HashMap<>();
        board.put(Square.of("b7"), Pawn.getInstance(Team.WHITE));
        board.put(Square.of("c7"), Pawn.getInstance(Team.WHITE));
        board.put(Square.of("a6"), King.getInstance(Team.WHITE));
        board.put(Square.of("c5"), Pawn.getInstance(Team.BLACK));
        board.put(Square.of("e8"), Knight.getInstance(Team.WHITE));
        board.put(Square.of("f6"), Queen.getInstance(Team.BLACK));
        board.put(Square.of("f3"), Pawn.getInstance(Team.BLACK));
        board.put(Square.of("g6"), King.getInstance(Team.BLACK));
        board.put(Square.of("g2"), Pawn.getInstance(Team.WHITE));

        Piece piece = Rook.getInstance(Team.BLACK);
        Set<Square> availableSquares = piece.findMovableAreas(Square.of("c6"), board);

        assertThat(availableSquares.contains(Square.of(input))).isTrue();
        assertThat(availableSquares.size()).isEqualTo(5);
    }


}
