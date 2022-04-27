package chess.domain;

import static chess.domain.state.Turn.WHITE_TURN;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChessGameTest {

    @Test
    @DisplayName("체스 게임 제목이 20자를 넘어가는 경우")
    void overMaxLengthTitle(){
        String title = "123456789012345678901";
        String password = "password";

        assertThatThrownBy(() -> new ChessGame(WHITE_TURN.name(), title, password))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("체스 게임의 제목은 20자 이하입니다.");
    }

    @Test
    @DisplayName("체스 게임의 비밀번호가 틀린 경우")
    void validateWrongPassword(){
        String title = "title";
        String password = "password";
        ChessGame chessGame = new ChessGame(WHITE_TURN.name(), title, password);

        assertThatThrownBy(() -> chessGame.validatePassword("wrong password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("체스 게임의 비밀번호가 틀렸습니다.");
    }
}
