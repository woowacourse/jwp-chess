package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.controller.dto.request.CreateGameRequest;
import chess.controller.dto.response.ChessGameResponse;
import chess.controller.dto.response.ChessGamesResponse;
import chess.dao.FakeGameDao;
import chess.dao.FakePieceDao;
import chess.dao.GameDao;
import chess.dao.PieceDao;
import chess.domain.GameState;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ChessServiceTest {

    private final GameDao gameDao = new FakeGameDao();
    private final PieceDao pieceDao = new FakePieceDao();

    private final ChessService chessService = new ChessService(gameDao, pieceDao);

    private static final CreateGameRequest CREAT_GAME_REQUEST = new CreateGameRequest("game", "password");
    private static final Long TEST_GAME_ID_1 = 1L;
    private static final Long TEST_GAME_ID_2 = 2L;

    @DisplayName("게임 생성 테스트")
    @Test
    void create_Game() {
        chessService.createGame(TEST_GAME_ID_1, CREAT_GAME_REQUEST);
    }

    @DisplayName("생성되어 있는 게임 리스트를 조회할 수 있다.")
    @Test
    void load_Game_List() {
        chessService.createGame(TEST_GAME_ID_1, CREAT_GAME_REQUEST);
        chessService.createGame(TEST_GAME_ID_2, new CreateGameRequest("game2", "password"));

        ChessGamesResponse response = chessService.findAllGameIds();

        assertThat(response.getGames().size()).isEqualTo(2);
    }

    @DisplayName("게임을 불러오면 저장된 상태 그대로 나온다.")
    @Test
    void load_Game() {
        chessService.createGame(TEST_GAME_ID_1, CREAT_GAME_REQUEST);

        ChessGameResponse chessGameResponse = chessService.loadGame(TEST_GAME_ID_1);

        assertThat(chessGameResponse.getGameState()).isEqualTo(GameState.READY);
    }

    @DisplayName("게임 시작 요청이 들어오면 게임을 시작한다.")
    @Test
    void start_Game() {
        chessService.createGame(TEST_GAME_ID_1, CREAT_GAME_REQUEST);

        ChessGameResponse chessGameResponse = chessService.startGame(TEST_GAME_ID_1);

        assertThat(chessGameResponse.getGameState()).isEqualTo(GameState.WHITE_RUNNING);
    }

    @DisplayName("초기화 요청이 들어오면 게임을 새로 생성한다.")
    @Test
    void restart_Game() {
        chessService.createGame(TEST_GAME_ID_1, CREAT_GAME_REQUEST);

        ChessGameResponse chessGameResponse = chessService.resetGame(TEST_GAME_ID_1);

        assertThat(chessGameResponse.getGameState()).isEqualTo(GameState.READY);
    }

    @Nested
    @DisplayName("DELETE - 게임 삭제 테스트")
    class DeleteTest {

        @DisplayName("올바른 삭제 요청이 들어오면 게임을 삭제한다.")
        @Test
        void delete_Game_Success() {
            chessService.createGame(TEST_GAME_ID_1, CREAT_GAME_REQUEST);
            gameDao.updateState(TEST_GAME_ID_1, GameState.WHITE_WIN);

            chessService.deleteGame(TEST_GAME_ID_1, "password");

            assertThat(chessService.findAllGameIds().getGames().size()).isEqualTo(0);
        }

        @DisplayName("게임이 종료되지 않은 상태에서는 삭제할 수 없다.")
        @Test
        void cannot_Delete_Game() {
            chessService.createGame(TEST_GAME_ID_1, CREAT_GAME_REQUEST);

            assertThatThrownBy(() -> chessService.deleteGame(TEST_GAME_ID_1, "password"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("게임이 종료되기 전에는 삭제할 수 없습니다.");
        }

        @DisplayName("비밀번호가 틀리면 종료할 수 없다.")
        @Test
        void authentication_Fail() {
            chessService.createGame(TEST_GAME_ID_1, CREAT_GAME_REQUEST);

            assertThatThrownBy(() -> chessService.deleteGame(TEST_GAME_ID_1, "wrong"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("비밀번호가 일치하지 않습니다.");
        }
    }
}
