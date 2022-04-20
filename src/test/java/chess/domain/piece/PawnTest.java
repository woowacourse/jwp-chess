package chess.domain.piece;

import static chess.domain.ChessFixtures.C4;
import static chess.domain.ChessFixtures.C5;
import static chess.domain.ChessFixtures.C6;
import static chess.domain.ChessFixtures.D7;
import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.Board;
import chess.domain.board.coordinate.Coordinate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class PawnTest {

    private final Pawn pawnWhite = new Pawn(Symbol.PAWN, Team.WHITE);
    private final Pawn pawnBlack = new Pawn(Symbol.PAWN, Team.BLACK);
    private Board board;

    @BeforeEach
    void setupBoard() {
        board = Board.create();
    }

    @ParameterizedTest
    @CsvSource(value = {
            "c2,c3",
            "c2,c4",
            "c3,c4"
    })
    @DisplayName("폰은 한칸 또는 두칸 앞으로 이동할 수 있다.")
    void white_pawn_move(String from, String to) {
        boolean actual = pawnWhite.isMovable(board, Coordinate.of(from), Coordinate.of(to));
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("흰색팀 폰은 DOWN 방향으로 이동할 수 없다.")
    void white_pawn_not_move() {
        boolean actual = pawnWhite.isMovable(board, C6, C5);
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("검정팀 폰은 UP 방향으로 이동할 수 없다.")
    void black_pawn_not_move() {
        boolean actual = pawnBlack.isMovable(board, C4, C5);
        assertThat(actual).isFalse();
    }

    @Test
    @DisplayName("대각선에 적이 있다면 이동할 수 있다.")
    void pawn_move_diagonal() {
        boolean actual = pawnWhite.isMovable(board, C6, D7);
        assertThat(actual).isTrue();
    }
}
