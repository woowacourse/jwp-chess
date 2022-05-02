package chess.domain.piece;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.piece.detail.Team;
import chess.domain.piece.pawn.Pawn;
import chess.domain.piece.singlemove.King;
import chess.domain.piece.singlemove.Knight;
import chess.domain.square.Square;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class PieceTest {

    @DisplayName("기물의 위치를 변경한다")
    @Test
    void updateSquare() {
        final Piece piece = new Knight(Team.WHITE, Square.from("a1"));
        final Square to = Square.from("c2");
        piece.updateSquare(to);

        assertThat(piece.getSquare()).isEqualTo(to);
    }

    @DisplayName("기물의 색상을 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"BLACK", "WHITE"})
    void getTeam(final String rawTeam) {
        final Team team = Team.valueOf(rawTeam);
        final Piece piece = new Knight(team, Square.from("a1"));
        assertThat(piece.getTeam()).isEqualTo(team);
    }

    @DisplayName("같은 팀 기물 위치로 이동할 경우 예외를 발생한다.")
    @ParameterizedTest
    @CsvSource({"WHITE,c1", "BLACK,a1"})
    void canAttackMove(final String rawTeam, final String rawSquare) {
        final Square from = Square.from("b2");
        final Square to = Square.from(rawSquare);
        final Team team = Team.valueOf(rawTeam);
        final Piece pawn = Pawn.of(team, from);

        assertThatThrownBy(() -> pawn.canMove(new King(team, to)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("같은 팀 위치로는 이동할 수 없습니다.");
    }

    @DisplayName("기물의 색상이 검정색인지 확인한다.")
    @Test
    void isBlackPiece() {
        final Team team = Team.BLACK;
        final Piece piece = new Knight(team, Square.from("a1"));
        assertThat(piece.getTeam().isBlack()).isTrue();
    }

    @DisplayName("기물의 색상이 흰색인지 확인한다.")
    @Test
    void isWhitePiece() {
        final Team team = Team.WHITE;
        final Piece piece = new Knight(team, Square.from("a1"));
        assertThat(piece.getTeam().isWhite()).isTrue();
    }
}
