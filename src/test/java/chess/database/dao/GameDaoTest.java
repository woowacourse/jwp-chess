package chess.database.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import chess.database.dto.GameStateDto;
import chess.database.dto.RoomDto;
import chess.domain.game.GameState;
import chess.domain.game.Ready;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest
class GameDaoTest {

    private static final String TEST_ROOM_NAME = "TESTING";
    private static final String TEST_ROOM_PASSWORD = "1234";
    private static final String TEST_CREATION_ROOM_NAME = "TESTING2";
    private static final String TEST_CREATION_ROOM_PASSWORD = "4321";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private GameDao gameDao;
    private RoomDao roomDao;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao(jdbcTemplate);
        roomDao = new RoomDao(jdbcTemplate);
        GameState state = new Ready();
        RoomDto roomDto = roomDao.create(new RoomDto(TEST_ROOM_NAME, TEST_ROOM_PASSWORD));
        gameDao.create(roomDto.getId(), state);
    }

    @Sql("/sql/chess-test.sql")
    @Test
    @DisplayName("게임을 생성한다.")
    public void createGame() {
        GameState state = new Ready();
        RoomDto roomDto = roomDao.create(
            new RoomDto(TEST_CREATION_ROOM_NAME, TEST_CREATION_ROOM_PASSWORD));
        assertThatCode(() -> gameDao.create(roomDto.getId(), state))
            .doesNotThrowAnyException();
        int findId = roomDao.findByName(TEST_CREATION_ROOM_NAME).getId();
        gameDao.removeGame(findId);
        roomDao.delete(findId);
    }

    @Sql("/sql/chess-test.sql")
    @Test
    @DisplayName("방 이름으로 게임 상태와 턴 색깔을 조회한다.")
    public void insert() {
        RoomDto roomDto = roomDao.findByName(TEST_ROOM_NAME);

        GameStateDto stateAndColor = gameDao.readStateAndColor(roomDto.getId());

        String stateString = stateAndColor.getState().name();
        String colorString = stateAndColor.getTurnColor().name();

        Assertions.assertAll(
            () -> assertThat(stateString).isEqualTo("READY"),
            () -> assertThat(colorString).isEqualTo("WHITE")
        );
    }

    @Sql("/sql/chess-test.sql")
    @Test
    @DisplayName("방 이름으로 게임 상태와 턴 색깔을 수정한다.")
    public void update() {
        GameState state = new Ready();
        GameState started = state.start();

        RoomDto roomDto = roomDao.findByName(TEST_ROOM_NAME);
        gameDao.updateState(roomDto.getId(), started.getState(), state.getTurnColor());

        GameStateDto stateAndColor = gameDao.readStateAndColor(roomDto.getId());

        String stateString = stateAndColor.getState().name();
        String colorString = stateAndColor.getTurnColor().name();

        assertThat(stateString).isEqualTo("RUNNING");
        assertThat(colorString).isEqualTo("WHITE");
    }
}
