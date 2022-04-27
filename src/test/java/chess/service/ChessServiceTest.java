package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.controller.dto.GameDto;
import chess.controller.dto.response.ChessGameResponse;
import chess.domain.GameState;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
    private final String password = "password";

    @BeforeEach
    void create_Account() {
        gameId = chessService.getGameId("name", password);
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

    @DisplayName("모든 게임 방의 이름들을 가져온다.")
    @Test
    void find_All_Games() {
        List<GameDto> games = chessService.findAllGames();
        List<String> expectedNames = games.stream()
                .map(GameDto::getName)
                .collect(Collectors.toList());
        assertThat(expectedNames).isEqualTo(List.of("name"));
    }

    @DisplayName("선택한 게임 방의 비밀번호가")
    @Nested
    class PasswordTest {

        @DisplayName("올바른 비밀번호면 ture 를 반환한다.")
        @Test
        void valid_Password_Test() {
            assertThat(chessService.checkPassword(gameId, password)).isTrue();
        }

        @DisplayName("올바르지 않은 비밀번호면 false 를 반환한다.")
        @Test
        void invalid_Password_Test() {
            assertThat(chessService.checkPassword(gameId, "wrongPassword")).isFalse();
        }
    }
}
