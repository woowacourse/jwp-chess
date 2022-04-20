package chess.domain.piece.pawn;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.piece.Blank;
import chess.domain.piece.Piece;
import chess.domain.piece.detail.Direction;
import chess.domain.piece.detail.Team;
import chess.domain.piece.singlemove.King;
import chess.domain.square.Square;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class BlackPawnTest {

    @DisplayName("검은색 폰이 움직일 수 있는지 확인한다.")
    @ParameterizedTest
    @CsvSource({"b2,S"})
    void canMove(final String rawSquare, final String rawDirection) {
        final Square from = Square.from(rawSquare);
        final Piece pawn = new BlackPawn(Team.BLACK, from);
        final Direction direction = Direction.valueOf(rawDirection);
        final Square to = from.next(direction);

        assertThat(pawn.canMove(new Blank(Team.NONE, to))).isTrue();
    }

    @DisplayName("검은색 폰이 공격할 수 있는지 확인한다.")
    @ParameterizedTest
    @CsvSource({"b2,SW", "b2,SE"})
    void canAttackMove(final String rawSquare, final String rawDirection) {
        final Square from = Square.from(rawSquare);
        final Piece pawn = new BlackPawn(Team.BLACK, from);
        final Direction direction = Direction.valueOf(rawDirection);
        final Square to = from.next(direction);

        assertThat(pawn.canMove(new King(Team.WHITE, to))).isTrue();
    }
}
