package chess.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.state.ChessGameState;
import chess.domain.state.RunningState;
import java.util.HashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChessGameTest {

    @Test
    @DisplayName("입력된 패스워드와 다르면 예외 발생")
    void checkPassword() {
        ChessGameState gameState = new RunningState(new ChessBoard(new HashMap<>()), Color.WHITE);
        ChessGame chessGame = new ChessGame(1L, "title", "password", gameState);

        assertThatThrownBy(() -> chessGame.checkPassword("errorPassword"))
            .isInstanceOf(IllegalStateException.class)
            .hasMessage("비밀번호가 일치하지 않습니다.");
    }
}
