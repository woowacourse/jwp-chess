package chess.domain.piece.singlemove;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import chess.domain.piece.Blank;
import chess.domain.piece.Piece;
import chess.domain.piece.detail.Direction;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;

class KnightTest {

    @DisplayName("나이트가 움직일 수 있는지 확인한다.")
    @ParameterizedTest
    @CsvSource({"a1,NNE", "a1,ENE", "a2,ESE", "a3,SSE", "c2,WSW", "c1,WNW", "b1,NNW"})
    void canMove(final String rawSquare, final String rawDirection) {
        final Square from = Square.from(rawSquare);
        final Knight knight = new Knight(Team.WHITE, from);
        final Direction direction = Direction.valueOf(rawDirection);
        final Square to = from.next(direction);

        Assertions.assertThat(knight.canMove(new Blank(Team.NONE, to))).isTrue();
    }

    @DisplayName("나이트가 움직이면 새로운 위치로의 객체를 반환한다.")
    @Test
    void getSquareOfMovedKnight() {
        final Piece piece = new Knight(Team.WHITE, Square.from("a1"));
        final Square to = Square.from("a2");
        final Piece newPiece = piece.moveTo(to);

        assertThat(newPiece.getSquare()).isEqualTo(to);
    }

    @DisplayName("나이트에 킹인지 물어봤을 때 false를 반환한다.")
    @Test
    void isKnightKing() {
        final Piece piece = new Knight(Team.WHITE, Square.from("a1"));
        assertThat(piece.isKing()).isFalse();
    }
}
