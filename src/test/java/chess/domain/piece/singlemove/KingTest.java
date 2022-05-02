package chess.domain.piece.singlemove;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.piece.Blank;
import chess.domain.piece.Piece;
import chess.domain.piece.detail.Direction;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class KingTest {

    @DisplayName("킹이 움직일 수 있는지 확인한다.")
    @ParameterizedTest
    @CsvSource({"a1,N", "a1,NE", "a1,E", "a2,SE", "a2,S", "b2,SW", "b1,NW"})
    void canMove(final String rawSquare, final String rawDirection) {
        final Square from = Square.from(rawSquare);
        final King king = new King(Team.WHITE, from);
        final Direction direction = Direction.valueOf(rawDirection);
        final Square to = from.next(direction);

        Assertions.assertThat(king.canMove(new Blank(Team.NONE, to))).isTrue();
    }

    @DisplayName("킹이 움직이면 새로운 위치로의 객체를 반환한다.")
    @Test
    void getSquareOfMovedKing() {
        final Piece piece = new King(Team.WHITE, Square.from("a1"));
        final Square to = Square.from("a2");
        final Piece newPiece = piece.moveTo(to);

        assertThat(newPiece.getSquare()).isEqualTo(to);
    }

    @DisplayName("킹에 킹인지 물어봤을 때 true를 반환한다.")
    @Test
    void isKing() {
        final Piece piece = new King(Team.WHITE, Square.from("a1"));
        assertThat(piece.isKing()).isTrue();
    }
}
