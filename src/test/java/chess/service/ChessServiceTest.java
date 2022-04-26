package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.controller.dto.response.ChessGameResponse;
import chess.domain.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ChessServiceTest {

    @Autowired
    private ChessService chessService;

    private int gameId;

    @BeforeEach
    void create_Account() {
        gameId = chessService.getGameId("name", "password");
    }

    @DisplayName("게임 생성 테스트")
    @Test
    void create_Game() {
        chessService.createGame(gameId);
    }

    @DisplayName("게임을 불러오면 저장된 상태 그대로 나온다.")
    @Test
    void load_Game() {
        chessService.createGame(gameId);

        ChessGameResponse chessGameResponse = chessService.loadGame(gameId);

        assertThat(chessGameResponse.getGameState()).isEqualTo(GameState.READY);
    }

    @DisplayName("재시작 요청이 들어오면 게임을 새로 생성한다.")
    @Test
    void restart_Game() {
        chessService.createGame(gameId);

        ChessGameResponse chessGameResponse = chessService.restartGame(gameId);

        assertThat(chessGameResponse.getGameState()).isEqualTo(GameState.READY);
    }
}
