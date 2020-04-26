package wooteco.chess.domain.piece;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PieceTest {

    @Test
    @DisplayName("Piece 이름으로 생성 테스트")
    void create() {
        assertThat(Piece.of("blackking")).isInstanceOf(Piece.class);
    }


    @Test
    @DisplayName("상대편 확인 테스트")
    void isEnemy() {
        Piece pieceA = Piece.of("blackking");
        Piece pieceB = Piece.of("whiteking");
        assertThat(pieceA.isEnemy(pieceB)).isTrue();
    }

    @Test
    @DisplayName("팀 확인 테스트")
    void isTeamOf() {
        Piece piece = Piece.of("blackking");
        assertThat(piece.isTeamOf(Team.BLACK)).isTrue();
    }

    @Test
    @DisplayName("Piece 이름으로 생성 테스트 - 유효하지 않은 이름")
    void create_IfInvalidName_ThrowException() {
        Piece piece = new King(null);
        assertThatThrownBy(() -> piece.isTeamOf(Team.WHITE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("유효하지 않은 팀입니다.");
    }

    @Test
    @DisplayName("말 점수 확인 테스트")
    void getScore() {
        Piece piece = Piece.of("blackqueen");
        assertThat(piece.getScore()).isEqualTo(9);
    }
}
