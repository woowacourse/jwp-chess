package chess.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.dto.GameCreationRequest;
import chess.exception.InvalidPasswordException;
import chess.exception.RunningGameDeletionException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ChessServiceTest {

    @Autowired
    ChessService chessService;

    @Test
    @DisplayName("비밀번호가 다르면 게임방을 삭제할 수 없다")
    void cannotDeleteWithWrongPassword() {
        chessService.addChessGame(new GameCreationRequest("test", "1234"));

        assertThatThrownBy(() -> chessService.deleteGame(1, "123"))
                .isInstanceOf(InvalidPasswordException.class);
    }

    @Test
    @DisplayName("진행중인 게임방을 삭제할 수 없다")
    void cannotDeleteRunningGame() {
        chessService.addChessGame(new GameCreationRequest("test", "1234"));

        assertThatThrownBy(() -> chessService.deleteGame(1, "1234"))
                .isInstanceOf(RunningGameDeletionException.class);
    }
}
