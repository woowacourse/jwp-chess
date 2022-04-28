package chess.service;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import chess.database.dao.FakeBoardDao;
import chess.database.dao.FakeGameDao;
import chess.database.dto.GameStateDto;
import chess.domain.game.GameState;
import chess.domain.game.Ready;
import chess.dto.Arguments;
import chess.dto.RoomRequest;

@SpringBootTest
class GameServiceTest {

    public static final String TEST_ROOM_NAME = "TEST-GAME";
    private final FakeGameDao gameDao = new FakeGameDao();
    private final FakeBoardDao boardDao = new FakeBoardDao();

    @Autowired
    private PasswordEncoder encoder;

    private GameService service;
    private Long id;

    @BeforeEach
    void setUp() {
        service = new GameService(gameDao, boardDao, encoder);
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

    @Test
    @DisplayName("게임 목록을 얻는다.")
    public void readGames() {
        // given & when
        final Map<Long, String> roomIdAndNames = service.readGameRooms();
        // then
        assertThat(roomIdAndNames).containsExactly(Map.entry(1L, "TEST-GAME"));
    }

    @Test
    @DisplayName("게임을 삭제한다.")
    public void deleteGame() {
        // given & when
        String password = "TEST-PASSWORD";

        // then
        assertThatCode(() -> service.deleteGame(new RoomRequest(TEST_ROOM_NAME, password)))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("잘못된 패스워드를 입력하면 게임을 삭제할 수 없다.")
    public void deleteGameWithWrongPassword() {
        // given
        String password = "WRONG-PASSWORD";

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> service.deleteGame(new RoomRequest(TEST_ROOM_NAME, password)));
    }

    @Test
    @DisplayName("진행중인 게임은 삭제할 수 없다.")
    public void deleteRunningGame() {

        // given & when
        String password = "TEST-PASSWORD";
        service.startGame(id);

        // when
        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(() -> service.deleteGame(new RoomRequest(TEST_ROOM_NAME, password)));

        // then
    }

    @AfterEach
    void setDown() {
        gameDao.removeGame(id);
        boardDao.removeBoard(id);
    }
}