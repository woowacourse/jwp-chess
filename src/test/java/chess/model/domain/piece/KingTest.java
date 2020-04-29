package chess.model.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.model.domain.board.CastlingSetting;
import chess.model.domain.board.Square;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class KingTest {

    @Test
    @DisplayName("Null이 of에 들어갔을 때 예외 발생")
    void validNotNull() {
        assertThatThrownBy(() -> King.getPieceInstance(null))
            .isInstanceOf(NullPointerException.class)
            .hasMessageContaining("Null");
    }

    @ParameterizedTest
    @ValueSource(strings = {"e6", "e8", "f6", "f7", "f8", "d6", "d7", "d8"})
    @DisplayName("말의 위치(king)를 받고 말의 종류에 따라 이동할 수 있는 칸 리스트 반환")
    void calculateScopeKing(String input) {
        Piece piece = King.getPieceInstance(Team.BLACK);
        Set<Square> availableSquares = piece.getAllMovableArea(Square.of("e7"));
        assertThat(availableSquares.contains(Square.of(input))).isTrue();
        assertThat(availableSquares.size()).isEqualTo(8);
    }

    @ParameterizedTest
    @ValueSource(strings = {"a5", "a7", "b5", "b7"})
    @DisplayName("판의 정보를 가져와서 king이 갈 수 있는 칸에 장애물이 있는지 판단하여 이동할 수 있는 리스트 반환하는 테스트")
    void movableKingSquareTest(String input) {
        Map<Square, Piece> board = new HashMap<>();
        board.put(Square.of("a5"), Pawn.getInstance(Team.WHITE));
        board.put(Square.of("b6"), Pawn.getInstance(Team.BLACK));
        Piece piece = King.getPieceInstance(Team.BLACK);
        Set<Square> availableSquares = piece.getMovableArea(Square.of("a6"), board);

        assertThat(availableSquares.contains(Square.of(input))).isTrue();
        assertThat(availableSquares.size()).isEqualTo(4);
    }

    @DisplayName("KING Castling 가능한지 확인")
    @Test
    void checkCastling() {
        Map<Square, Piece> board = new HashMap<>();
        board.put(Square.of("a8"), Rook.getPieceInstance(Team.BLACK));
        board.put(Square.of("h8"), Rook.getPieceInstance(Team.BLACK));
        board.put(Square.of("a1"), Rook.getPieceInstance(Team.WHITE));
        board.put(Square.of("h1"), Rook.getPieceInstance(Team.WHITE));

        Piece piece = King.getPieceInstance(Team.BLACK);
        Set<Square> availableSquares = piece.getMovableArea(Square.of("e8"), board,
            CastlingSetting.getCastlingElements());

        assertThat(availableSquares.contains(Square.of("c8"))).isTrue();
        assertThat(availableSquares.contains(Square.of("g8"))).isTrue();

        piece = King.getPieceInstance(Team.WHITE);
        availableSquares = piece.getMovableArea(Square.of("e1"), board,
            CastlingSetting.getCastlingElements());

        assertThat(availableSquares.contains(Square.of("c1"))).isTrue();
        assertThat(availableSquares.contains(Square.of("g1"))).isTrue();

        board.put(Square.of("d8"), Rook.getPieceInstance(Team.BLACK));

        piece = King.getPieceInstance(Team.BLACK);
        availableSquares = piece.getMovableArea(Square.of("e8"), board,
            CastlingSetting.getCastlingElements());

        assertThat(availableSquares.contains(Square.of("c8"))).isFalse();
        assertThat(availableSquares.contains(Square.of("g8"))).isTrue();

        piece = King.getPieceInstance(Team.BLACK);
        availableSquares = piece.getMovableArea(Square.of("e7"), board,
            CastlingSetting.getCastlingElements());

        assertThat(availableSquares.contains(Square.of("c7"))).isFalse();
        assertThat(availableSquares.contains(Square.of("g7"))).isFalse();

        piece = King.getPieceInstance(Team.WHITE);
        Set<CastlingSetting> castlingSettings = CastlingSetting.getCastlingElements();
        castlingSettings.remove(CastlingSetting.WHITE_KING_BEFORE);
        availableSquares = piece.getMovableArea(Square.of("e1"), board,
            castlingSettings);

        assertThat(availableSquares.contains(Square.of("c1"))).isFalse();
        assertThat(availableSquares.contains(Square.of("g1"))).isFalse();
    }
}
