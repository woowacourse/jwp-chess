package chess.domain.piece.multiplemove;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import chess.domain.piece.Blank;
import chess.domain.piece.Piece;
import chess.domain.piece.detail.Team;
import chess.domain.square.Square;

class BishopTest {

    @DisplayName("비숍이 갈 수 있는 위치인지 확인한다.")
    @ParameterizedTest
    @CsvSource({"d4,a1", "d4,g1", "d4,a7", "d4,h8"})
    void canMove(final String rawFrom, final String rawTo) {
        final Square from = Square.from(rawFrom);
        final Square to = Square.from(rawTo);
        final Piece bishop = new Bishop(Team.WHITE, from);

        Assertions.assertThat(bishop.canMove(new Blank(Team.NONE, to))).isTrue();
    }

    @DisplayName("비숍이 움직이면 새로운 위치로의 객체를 반환한다.")
    @Test
    void getSquareOfMovedBishop() {
        final Piece piece = new Bishop(Team.WHITE, Square.from("a1"));
        final Square to = Square.from("a2");
        final Piece newPiece = piece.moveTo(to);

        assertThat(newPiece.getSquare()).isEqualTo(to);
    }

    @DisplayName("비숍에 킹인지 물어봤을 때 false를 반환한다.")
    @Test
    void isBishopKing() {
        final Piece piece = new Bishop(Team.WHITE, Square.from("a1"));
        assertThat(piece.isKing()).isFalse();
    }
}
