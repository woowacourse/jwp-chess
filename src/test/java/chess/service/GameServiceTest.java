package chess.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import chess.database.dao.FakeGameDao;
import chess.database.dao.FakePieceDao;
import chess.database.dao.FakeRoomDao;
import chess.database.dao.GameDao;
import chess.database.dao.PieceDao;
import chess.database.dao.RoomDao;
import chess.domain.Room;
import chess.domain.game.GameState;
import chess.domain.game.Ready;
import chess.dto.RoomRequest;
import chess.dto.RouteRequest;
import chess.repository.GameRepository;
import chess.repository.RoomRepository;

@SpringBootTest
class GameServiceTest {

    public static final String TEST_ROOM_NAME = "TEST-GAME";

    @Autowired
    private PasswordEncoder encoder;

    private GameRepository gameRepository;
    private RoomRepository roomRepository;

    private GameService service;
    private Long roomId;
    private Long gameId;

    @BeforeEach
    void setUp() {
        RoomDao roomDao = new FakeRoomDao();
        GameDao gameDao = new FakeGameDao();
        PieceDao pieceDao = new FakePieceDao();

        roomRepository = new RoomRepository(roomDao, gameDao, pieceDao);
        gameRepository = new GameRepository(gameDao, pieceDao);
        service = new GameService(gameRepository, roomRepository, encoder);

        roomId = service.createNewGame(new RoomRequest("TEST-GAME", "TEST-PASSWORD"));
        gameId = gameRepository.findGameIdByRoomId(roomId);
    }

    @Test
    @DisplayName("새로운 방을 만든다.")
    public void createNewGame() {
        assertThatCode(() -> service.createNewGame(new RoomRequest("TEST-GAME2", "TEST-PASSWORD")))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("게임 목록을 얻는다.")
    public void readGames() {
        // given & when
        final List<Room> rooms = service.readGameRooms();
        // then
        assertThat(rooms.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("게임을 삭제한다.")
    public void deleteGame() {
        // given & when
        String password = "TEST-PASSWORD";
        final GameState state = gameRepository.findGameByRoomId(roomId);
        final Long gameId = gameRepository.findGameIdByRoomId(roomId);
        final GameState finished = state.finish();
        gameRepository.updateState(finished, gameId);

        // then
        assertThatCode(() -> service.removeRoom(roomId, new RoomRequest(TEST_ROOM_NAME, password)))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("잘못된 패스워드를 입력하면 게임을 삭제할 수 없다.")
    public void deleteGameWithWrongPassword() {
        // given
        String password = "WRONG-PASSWORD";

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> service.removeRoom(roomId, new RoomRequest(TEST_ROOM_NAME, password)));
    }

    @Test
    @DisplayName("진행중인 게임은 삭제할 수 없다.")
    public void deleteRunningGame() {
        // given & when
        String password = "TEST-PASSWORD";

        // when
        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(() -> service.removeRoom(roomId, new RoomRequest(TEST_ROOM_NAME, password)));

        // then
    }

    @Test
    @DisplayName("게임 상태를 얻는다.")
    public void readGameState() {
        // given
        // when
        GameState gameState = service.readGameState(gameId);
        // then
        assertThat(gameState).isInstanceOf(Ready.class);
    }

    @Test
    @DisplayName("게임을 시작한다.")
    public void startGame() {
        assertThatCode(() -> service.startGame(gameId))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("말을 움직인다.")
    public void moveBoard() {
        // given
        service.startGame(gameId);
        // when
        GameState moved = service.moveBoard(gameId, new RouteRequest("a2", "a3"));
        // then
        assertThat(moved).isNotNull();
    }

    @Test
    @DisplayName("게임을 종료한다.")
    public void finishGame() {
        // given & when

        // then
        assertThatCode(() -> service.finishGame(gameId))
            .doesNotThrowAnyException();
    }

    @AfterEach
    void setDown() {
        roomRepository.deleteRoom(roomId);
    }
}