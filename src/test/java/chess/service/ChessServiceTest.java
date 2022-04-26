package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.controller.dto.request.CreateGameRequest;
import chess.controller.dto.response.ChessGameResponse;
import chess.controller.dto.response.ChessGamesResponse;
import chess.controller.dto.response.EndResponse;
import chess.domain.GameState;
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

    private static final CreateGameRequest CREAT_GAME_REQUEST = new CreateGameRequest("game", "password");

    @DisplayName("게임 생성 테스트")
    @Test
    void create_Game() {
        chessService.createGame(1, CREAT_GAME_REQUEST);
    }

    @DisplayName("생성되어 있는 게임 리스트를 조회할 수 있다.")
    @Test
    void load_Game_List() {
        chessService.createGame(1, CREAT_GAME_REQUEST);
        chessService.createGame(2, new CreateGameRequest("game2", "password"));

        ChessGamesResponse response = chessService.findAllGameIds();

        assertThat(response.getGames().size()).isEqualTo(2);
    }

    @DisplayName("게임을 불러오면 저장된 상태 그대로 나온다.")
    @Test
    void load_Game() {
        chessService.createGame(1, CREAT_GAME_REQUEST);

        ChessGameResponse chessGameResponse = chessService.loadGame(1);

        assertThat(chessGameResponse.getGameState()).isEqualTo(GameState.READY);
    }

    @DisplayName("게임 시작 요청이 들어오면 게임을 시작한다.")
    @Test
    void start_Game() {
        chessService.createGame(1, CREAT_GAME_REQUEST);

        ChessGameResponse chessGameResponse = chessService.startGame(1);

        assertThat(chessGameResponse.getGameState()).isEqualTo(GameState.WHITE_RUNNING);
    }

    @DisplayName("초기화 요청이 들어오면 게임을 새로 생성한다.")
    @Test
    void restart_Game() {
        chessService.createGame(1, CREAT_GAME_REQUEST);

        ChessGameResponse chessGameResponse = chessService.resetGame(1);

        assertThat(chessGameResponse.getGameState()).isEqualTo(GameState.READY);
    }

    @DisplayName("종료 요청이 들어오면 게임을 삭제한다.")
    @Test
    void end_Game() {
        chessService.createGame(1, CREAT_GAME_REQUEST);

        EndResponse endResponse = chessService.endGame(1);

        assertThat(endResponse.getMessage()).isEqualTo("게임이 종료되었습니다.");
    }
}
