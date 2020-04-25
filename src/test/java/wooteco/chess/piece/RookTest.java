package wooteco.chess.piece;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import wooteco.chess.domain.coordinate.Vector;
import wooteco.chess.domain.piece.Blank;
import wooteco.chess.domain.piece.Piece;
import wooteco.chess.domain.piece.Rook;
import wooteco.chess.domain.piece.Team;

import static org.assertj.core.api.Assertions.assertThat;

class RookTest {
    @DisplayName("룩은 직선으로 무한정 움직일 수 있다.")
    @ParameterizedTest
    @CsvSource(value = {"3,0,true", "3,-1,false"})
    void canMove(int file, int rank, boolean expect) {
        Piece rook = new Rook(Team.BLACK);
        assertThat(rook.canMove(new Vector(file, rank), new Blank())).isEqualTo(expect);
    }
}