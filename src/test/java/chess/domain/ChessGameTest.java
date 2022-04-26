package chess.domain;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.ChessGame;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChessGameTest {

    @Test
    @DisplayName("입력된 값과 비밀번호가 일치하는지 확인한다")
    void matchPassword() {
        ChessGame chessGame = new ChessGame("1", "test", "1234");
        assertThat(chessGame.isMatched("1234")).isTrue();
    }
}
