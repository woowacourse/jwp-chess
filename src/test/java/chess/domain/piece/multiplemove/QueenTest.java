package chess.domain.piece.multiplemove;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.piece.Blank;
import chess.domain.piece.Piece;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class QueenTest {

    @DisplayName("퀸이 갈 수 있는 위치인지 확인한다.")
    @ParameterizedTest
    @CsvSource({
            "d4,d8", "d4,h4", "d4,d1", "d4,a4",
            "d4,a1", "d4,g1", "d4,a7", "d4,h8"
    })
    void canMove(final String rawFrom, final String rawTo) {
        final Square from = Square.from(rawFrom);
        final Square to = Square.from(rawTo);
        final Piece queen = new Queen(Team.WHITE, from);

        assertThat(queen.canMove(new Blank(Team.NONE, to))).isTrue();
    }

    @DisplayName("퀸이 움직이면 새로운 위치로의 객체를 반환한다.")
    @Test
    void getSquareOfMovedQueen() {
        final Piece piece = new Queen(Team.WHITE, Square.from("a1"));
        final Square to = Square.from("a2");
        final Piece newPiece = piece.moveTo(to);

        assertThat(newPiece.getSquare()).isEqualTo(to);
    }

    @DisplayName("퀸에 킹인지 물어봤을 때 false를 반환한다.")
    @Test
    void isQueenKing() {
        final Piece piece = new Queen(Team.WHITE, Square.from("a1"));
        assertThat(piece.isKing()).isFalse();
    }
}
