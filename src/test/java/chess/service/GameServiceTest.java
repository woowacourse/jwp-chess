package chess.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import chess.database.dao.FakeBoardDao;
import chess.database.dao.FakeGameDao;
import chess.database.dto.GameStateDto;
import chess.domain.game.GameState;
import chess.domain.game.Ready;
import chess.dto.Arguments;
import chess.dto.RoomRequest;

class GameServiceTest {

    private final FakeGameDao gameDao = new FakeGameDao();
    private final FakeBoardDao boardDao = new FakeBoardDao();

    @Autowired
    private PasswordEncoder encoder;

    private final GameService service = new GameService(gameDao, boardDao, encoder);
    private Long id;

    @BeforeEach
    void setUp() {
        id = service.createNewGame(new RoomRequest("TEST-GAME", "TEST-PASSWORD"));
    }

    @Test
    @DisplayName("새로운 방을 만든다.")
    public void createNewGame() {
        assertThatCode(() -> service.createNewGame(new RoomRequest("TEST-GAME2", "TEST-PASSWORD")))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("게임 상태를 얻는다.")
    public void readGameState() {
        // given
        Long roomId = id; 
        // when
        GameState gameState = service.readGameState(roomId);
        // then
        assertThat(gameState).isInstanceOf(Ready.class);
    }

    @Test
    @DisplayName("게임을 시작한다.")
    public void startGame() {
        // given & when
        Long roomId = id;
        // then
        assertThatCode(() -> service.startGame(roomId))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("말을 움직인다.")
    public void moveBoard() {
        // given
        Long roomId = id;
        Arguments arguments = Arguments.ofArray(new String[] {"a2", "a4"}, 0);

        GameState gameState = service.readGameState(roomId);
        GameState started = gameState.start();
        gameDao.updateState(GameStateDto.of(started), id);

        // when
        GameState moved = service.moveBoard(roomId, arguments);
        // then
        assertThat(moved).isNotNull();
    }

    @Test
    @DisplayName("게임을 종료한다.")
    public void finishGame() {
        // given & when
        Long roomId = id;

        // then
        assertThatCode(() -> service.finishGame(roomId))
            .doesNotThrowAnyException();
    }

    @AfterEach
    void setDown() {
        gameDao.removeGame(id);
        boardDao.removeBoard(id);
    }
}