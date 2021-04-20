package chess.webdao;

import chess.domain.piece.Knight;
import chess.domain.piece.Piece;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DaoToPieceTest {
    @Test
    @DisplayName("팀, 기물, 첫 수 정보가 정상적으로 넘어가면, 알맞은 도메인 기물을 생성한다")
    void generatePieceTest() {
        final Piece resultPiece = DaoToPiece.generatePiece("black", "Knight", true);
        assertThat(resultPiece).isInstanceOf(Knight.class);
    }

    @Test
    @DisplayName("팀, 기물, 첫 수 정보가 정상적으로 넘어가지 않으면, 예외가 발생한다")
    void generatePieceExceptionTest() {
        assertThatThrownBy(() -> DaoToPiece.generatePiece("red", "PowerRanger", false))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("기물 정보가 DB에 잘못 저장되어 있습니다.");
    }
}
