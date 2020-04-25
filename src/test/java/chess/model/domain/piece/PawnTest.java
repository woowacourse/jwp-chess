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
        assertThatThrownBy(() -> Pawn.getPieceInstance(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Null");
    }

    @Test
    @DisplayName("말의 위치(pawn)를 받고 말의 종류에 따라 이동할 수 있는 칸 리스트 반환")
    void calculateScopePawnBlack() {
        Piece pieceBlack = Pawn.getPieceInstance(Team.BLACK);
        Piece pieceWhite = Pawn.getPieceInstance(Team.WHITE);

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
        board.put(Square.of("a5"), Knight.getPieceInstance(Team.BLACK));
        board.put(Square.of("c5"), Knight.getPieceInstance(Team.WHITE));

        Piece pieceBlack = Pawn.getPieceInstance(Team.BLACK);
        Piece pieceWhite = Pawn.getPieceInstance(Team.WHITE);

        Set<Square> availableSquares = new HashSet<>();
        availableSquares.addAll(pieceWhite.getMovableArea(Square.of("a4"), board));
        availableSquares.addAll(pieceWhite.getMovableArea(Square.of("c4"), board));
        availableSquares.addAll(pieceBlack.getMovableArea(Square.of("a6"), board));
        availableSquares.addAll(pieceBlack.getMovableArea(Square.of("c6"), board));

        assertThat(availableSquares.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("폰이 두 칸 움직일 수 있는지 테스트")
    void movablePawnTwoSquareTest() {
        Map<Square, Piece> board = new HashMap<>();
        board.put(Square.of("b6"), Knight.getPieceInstance(Team.BLACK));

        Piece pieceBlack = Pawn.getPieceInstance(Team.BLACK);
        Piece pieceWhite = Pawn.getPieceInstance(Team.WHITE);

        Set<Square> availableSquaresBlack = pieceBlack
            .getMovableArea(Square.of("a7"), board);
        Set<Square> availableSquaresBlack2 = pieceBlack
            .getMovableArea(Square.of("b7"), board);
        Set<Square> availableSquaresWhite = pieceWhite
            .getMovableArea(Square.of("a2"), board);

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

        board.put(Square.of("b5"), Knight.getPieceInstance(Team.BLACK));
        board.put(Square.of("e5"), Knight.getPieceInstance(Team.BLACK));
        board.put(Square.of("f5"), Knight.getPieceInstance(Team.WHITE));

        Piece blackPawn = Pawn.getPieceInstance(Team.BLACK);
        Set<Square> availableSquares = blackPawn
            .getMovableArea(Square.of("c6"), board);
        assertThat(availableSquares.contains(Square.of("c5"))).isTrue();
        assertThat(availableSquares.size()).isEqualTo(1);

        availableSquares = blackPawn.getMovableArea(Square.of("f6"), board);
        assertThat(availableSquares.contains(Square.of("f5"))).isFalse();
        assertThat(availableSquares.size()).isEqualTo(0);

        availableSquares = blackPawn.getMovableArea(Square.of("e6"), board);
        assertThat(availableSquares.contains(Square.of("f5"))).isTrue();
        assertThat(availableSquares.size()).isEqualTo(1);

        availableSquares = blackPawn.getMovableArea(Square.of("g6"), board);
        assertThat(availableSquares.contains(Square.of("g5"))).isTrue();
        assertThat(availableSquares.contains(Square.of("f5"))).isTrue();
        assertThat(availableSquares.size()).isEqualTo(2);
    }

}
