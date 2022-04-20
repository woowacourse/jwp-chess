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

public class BlackTurnTest {

    private Board board;

    @BeforeEach
    void setupBoard() {
        board = Board.create();
    }

    @Test
    void start() {
        assertThatThrownBy(() -> new BlackTurn(board).start())
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void end() {
        assertThat(new BlackTurn(board).end()).isInstanceOf(End.class);
    }

    @Test
    void move_black() {
        State whiteTurn = new BlackTurn(board).move(A7, A6);
        Piece piece = whiteTurn.getBoard().getValue().get(A6);
        assertThat(piece).isInstanceOf(Pawn.class);
    }

    @Test
    @DisplayName("BlackTurn 상태에선 흰색 말을 움직이면 에러가 발생한다.")
    void move_white() {
        assertThatThrownBy(() -> new BlackTurn(board).move(A2, A4))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void is_finished() {
        assertThat(new BlackTurn(board).isFinished()).isFalse();
    }
}
