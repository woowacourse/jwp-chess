package chess.domain.game.state;

import static chess.domain.ChessFixtures.A2;
import static chess.domain.ChessFixtures.A4;
import static chess.domain.ChessFixtures.A6;
import static chess.domain.ChessFixtures.A7;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.board.Board;
import chess.domain.piece.Pawn;
import chess.domain.piece.Piece;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class WhiteTurnTest {

    private Board board;

    @BeforeEach
    void setupBoard() {
        board = Board.create();
    }

    @Test
    void start() {
        assertThatThrownBy(() -> new WhiteTurn(board).start())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void end() {
        assertThat(new WhiteTurn(board).end()).isInstanceOf(End.class);
    }

    @Test
    void move_white() {
        State blackTurn = new WhiteTurn(board).move(A2, A4);
        Piece piece = blackTurn.getBoard().getValue().get(A4);
        assertThat(piece).isInstanceOf(Pawn.class);
    }

    @Test
    @DisplayName("WhiteTurn 상태에서 검정 말을 움직이면 에러가 발생한다.")
    void move_black() {
        assertThatThrownBy(() -> new WhiteTurn(board).move(A7, A6))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void is_finished() {
        assertThat(new WhiteTurn(board).isFinished()).isFalse();
    }
}
