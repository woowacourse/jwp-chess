package chess.model.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.model.domain.board.Square;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PawnTest {

    @Test
    @DisplayName("Null이 of에 들어갔을 때 예외 발생")
    void validNotNull() {
        assertThatThrownBy(() -> Pawn.getInstance(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Null");
    }

    @Test
    @DisplayName("말의 위치(pawn)를 받고 말의 종류에 따라 이동할 수 있는 칸 리스트 반환")
    void calculateScopePawnBlack() {
        Piece pieceBlack = Pawn.getInstance(Team.BLACK);
        Piece pieceWhite = Pawn.getInstance(Team.WHITE);

        Set<Square> availableSquaresBlack = pieceBlack.getAllMovableArea(Square.of("a7"));
        Set<Square> availableSquaresWhite = pieceWhite.getAllMovableArea(Square.of("a6"));

        assertThat(availableSquaresBlack.contains(Square.of("a6"))).isTrue();
        assertThat(availableSquaresWhite.contains(Square.of("a7"))).isTrue();

        assertThat(availableSquaresBlack.size()).isEqualTo(1);
        assertThat(availableSquaresWhite.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("앞에 말이 있다면 못가는지 테스트")
    void canNotCaptureFrontPiece() {
        Map<Square, Piece> board = new HashMap<>();
        board.put(Square.of("a5"), Knight.getInstance(Team.BLACK));
        board.put(Square.of("c5"), Knight.getInstance(Team.WHITE));

        Piece pieceBlack = Pawn.getInstance(Team.BLACK);
        Piece pieceWhite = Pawn.getInstance(Team.WHITE);

        Set<Square> availableSquares = new HashSet<>();
        availableSquares.addAll(pieceWhite.findMovableAreas(Square.of("a4"), board));
        availableSquares.addAll(pieceWhite.findMovableAreas(Square.of("c4"), board));
        availableSquares.addAll(pieceBlack.findMovableAreas(Square.of("a6"), board));
        availableSquares.addAll(pieceBlack.findMovableAreas(Square.of("c6"), board));

        assertThat(availableSquares.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("폰이 두 칸 움직일 수 있는지 테스트")
    void movablePawnTwoSquareTest() {
        Map<Square, Piece> board = new HashMap<>();
        board.put(Square.of("b6"), Knight.getInstance(Team.BLACK));

        Piece pieceBlack = Pawn.getInstance(Team.BLACK);
        Piece pieceWhite = Pawn.getInstance(Team.WHITE);

        Set<Square> availableSquaresBlack = pieceBlack
            .findMovableAreas(Square.of("a7"), board);
        Set<Square> availableSquaresBlack2 = pieceBlack
            .findMovableAreas(Square.of("b7"), board);
        Set<Square> availableSquaresWhite = pieceWhite
            .findMovableAreas(Square.of("a2"), board);

        assertThat(availableSquaresBlack.contains(Square.of("a5"))).isTrue();
        assertThat(availableSquaresBlack.contains(Square.of("a6"))).isTrue();
        assertThat(availableSquaresWhite.contains(Square.of("a3"))).isTrue();
        assertThat(availableSquaresWhite.contains(Square.of("a4"))).isTrue();
        assertThat(availableSquaresBlack2.contains(Square.of("b5"))).isFalse();

        assertThat(availableSquaresBlack.size()).isEqualTo(2);
        assertThat(availableSquaresWhite.size()).isEqualTo(2);
        assertThat(availableSquaresBlack2.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("폰 대각선 이동 테스트")
    void movablePawnSquareTest() {
        Map<Square, Piece> board = new HashMap<>();

        board.put(Square.of("b5"), Knight.getInstance(Team.BLACK));
        board.put(Square.of("e5"), Knight.getInstance(Team.BLACK));
        board.put(Square.of("f5"), Knight.getInstance(Team.WHITE));

        Piece blackPawn = Pawn.getInstance(Team.BLACK);
        Set<Square> availableSquares = blackPawn
            .findMovableAreas(Square.of("c6"), board);
        assertThat(availableSquares.contains(Square.of("c5"))).isTrue();
        assertThat(availableSquares.size()).isEqualTo(1);

        availableSquares = blackPawn.findMovableAreas(Square.of("f6"), board);
        assertThat(availableSquares.contains(Square.of("f5"))).isFalse();
        assertThat(availableSquares.size()).isEqualTo(0);

        availableSquares = blackPawn.findMovableAreas(Square.of("e6"), board);
        assertThat(availableSquares.contains(Square.of("f5"))).isTrue();
        assertThat(availableSquares.size()).isEqualTo(1);

        availableSquares = blackPawn.findMovableAreas(Square.of("g6"), board);
        assertThat(availableSquares.contains(Square.of("g5"))).isTrue();
        assertThat(availableSquares.contains(Square.of("f5"))).isTrue();
        assertThat(availableSquares.size()).isEqualTo(2);
    }

}
