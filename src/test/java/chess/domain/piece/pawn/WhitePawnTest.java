package chess.domain.piece.pawn;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import chess.domain.piece.Blank;
import chess.domain.piece.Piece;
import chess.domain.piece.detail.Direction;
import chess.domain.piece.detail.Team;
import chess.domain.piece.singlemove.King;
import chess.domain.square.Square;

class WhitePawnTest {

    @DisplayName("흰색 폰이 움직일 수 있는지 확인한다.")
    @ParameterizedTest
    @CsvSource({"b2,N"})
    void canMove(final String rawSquare, final String rawDirection) {
        final Square from = Square.from(rawSquare);
        final Piece pawn = new WhitePawn(Team.WHITE, from);
        final Direction direction = Direction.valueOf(rawDirection);
        final Square to = from.next(direction);

        Assertions.assertThat(pawn.canMove(new Blank(Team.NONE, to))).isTrue();
    }

    @DisplayName("흰색 폰이 공격할 수 있는지 확인한다.")
    @ParameterizedTest
    @CsvSource({"b2,NW", "b2,NE"})
    void canAttackMove(final String rawSquare, final String rawDirection) {
        final Square from = Square.from(rawSquare);
        final Piece pawn = new WhitePawn(Team.WHITE, from);
        final Direction direction = Direction.valueOf(rawDirection);
        final Square to = from.next(direction);

        Assertions.assertThat(pawn.canMove(new King(Team.BLACK, to))).isTrue();
    }
}
