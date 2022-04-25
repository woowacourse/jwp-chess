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

class RookTest {

    @DisplayName("룩이 갈 수 있는 위치인지 확인한다.")
    @ParameterizedTest
    @CsvSource({"d4,d8", "d4,h4", "d4,d1", "d4,a4"})
    void canMove(final String rawFrom, final String rawTo) {
        final Square from = Square.from(rawFrom);
        final Square to = Square.from(rawTo);
        final Rook rook = new Rook(Team.WHITE, from);

        Assertions.assertThat(rook.canMove(new Blank(Team.NONE, to))).isTrue();
    }

    @DisplayName("룩이 움직이면 새로운 위치로의 객체를 반환한다.")
    @Test
    void getSquareOfMovedRook() {
        final Piece piece = new Rook(Team.WHITE, Square.from("a1"));
        final Square to = Square.from("a2");
        final Piece newPiece = piece.moveTo(to);

        assertThat(newPiece.getSquare()).isEqualTo(to);
    }

    @DisplayName("룩에 킹인지 물어봤을 때 false를 반환한다.")
    @Test
    void isRookKing() {
        final Piece piece = new Rook(Team.WHITE, Square.from("a1"));
        assertThat(piece.isKing()).isFalse();
    }
}
