package chess.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.dto.GameCreationDTO;
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
        chessService.addChessGame(new GameCreationDTO("test", "1234"));

        assertThatThrownBy(() -> chessService.deleteGame("1", "123"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
