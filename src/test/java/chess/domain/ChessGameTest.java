package chess.domain;

import static chess.ChessGameFixture.createEndChessGame;
import static chess.ChessGameFixture.createRunningChessGame;
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
        ChessGame chessGame = createRunningChessGame();

        assertThatThrownBy(() -> chessGame.validatePassword("wrong password"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("체스 게임의 비밀번호가 틀렸습니다.");
    }

    @Test
    @DisplayName("체스 게임이 아직 진행중인 상태에서 게임이 끝났는지 검증")
    void validateEndGame(){
        ChessGame chessGame = createRunningChessGame();

        assertThatThrownBy(chessGame::validateEndGame)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("아직 체스 게임이 진행 중입니다.");
    }

    @Test
    @DisplayName("체스 게임이 이미 종료된 상태에서 게임이 진행중인지 검증")
    void validateRunningGame(){
        ChessGame chessGame = createEndChessGame();

        assertThatThrownBy(chessGame::validateRunningGame)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 종료된 게임입니다.");
    }
}
