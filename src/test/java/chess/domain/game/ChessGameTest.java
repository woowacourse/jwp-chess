package chess.domain.game;

import static chess.domain.ChessFixtures.A2;
import static chess.domain.ChessFixtures.A4;
import static chess.domain.ChessFixtures.A5;
import static chess.domain.ChessFixtures.A7;
import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.InitialBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ChessGameTest {

    private ChessGame chessGame;
    
    @BeforeEach
    void setupChessGame() {
        chessGame = new ChessGame();
    }

    @Test
    @DisplayName("start를 하면 보드가 초기화된다.")
    void start() {
        chessGame.start();
        assertThat(chessGame.getBoard().getValue()).isEqualTo(InitialBoard.initialize());
    }

    @Test
    @DisplayName("흰색말 부터 움직일 수 있다.")
    void move_white_turn() {
        chessGame.start();
        chessGame.move(A2, A4);
        String team = chessGame.getBoard().getValue()
                .get(A4)
                .getTeam();
        assertThat(team).isEqualTo("WHITE");
    }

    @Test
    @DisplayName("흰색 말이 움직인 후엔 검은색 말만 움직일 수 있다.")
    void move_black_turn() {
        chessGame.start();
        chessGame.move(A2, A4);
        chessGame.move(A7, A5);
        String team = chessGame.getBoard()
                .getValue()
                .get(A5)
                .getTeam();
        assertThat(team).isEqualTo("BLACK");
    }

    @Test
    @DisplayName("end를 하면 게임이 종료된다.")
    void end() {
        chessGame.start();
        chessGame.end();

        assertThat(chessGame.isFinished()).isTrue();
    }
}
