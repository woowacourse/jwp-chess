package chess.database.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import chess.database.entity.GameEntity;
import chess.database.entity.RoomEntity;
import chess.domain.game.GameState;
import chess.domain.game.Ready;

@JdbcTest
class GameDaoTest {

    private static final Supplier<RuntimeException> RUNTIME_EXCEPTION_SUPPLIER =
        () -> new RuntimeException("[ERROR] 방이 존재하지 않습니다.");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private RoomDao roomDao;
    private GameDao dao;
    private Long roomId;
    private Long gameId;

    @BeforeEach
    void setUp() {
        roomDao = new JdbcRoomDao(dataSource, jdbcTemplate);
        roomId = roomDao.saveRoom(RoomEntity.from("TEST-ROOM-NAME", "TEST-PASSWORD"));

        dao = new JdbcGameDao(dataSource, jdbcTemplate);
        gameId = dao.saveGame(GameEntity.fromRoomId(new Ready(), roomId));
    }

    @Test
    @DisplayName("게임을 생성한다.")
    public void createGame() {
        // given
        final GameEntity gameEntity = GameEntity.fromRoomId(new Ready(), roomId);

        // when
        final Long savedId = dao.saveGame(gameEntity);

        // then
        assertThat(savedId).isNotNull();
    }

    @Test
    @DisplayName("ID로 게임 상태와 턴 색깔을 조회한다.")
    public void insert() {
        // given & when
        final GameEntity entity = dao.findGameById(gameId).orElseThrow(RUNTIME_EXCEPTION_SUPPLIER);
        // then
        Assertions.assertAll(
            () -> assertThat(entity.getState()).isEqualTo("READY"),
            () -> assertThat(entity.getTurnColor()).isEqualTo("WHITE")
        );
    }

    @Test
    @DisplayName("ID로 게임 상태와 턴 색깔을 수정한다.")
    public void update() {
        // given
        GameState state = new Ready();
        GameState started = state.start();

        // when
        dao.updateGame(GameEntity.from(started, gameId));
        final GameEntity gameEntity = dao.findGameById(gameId).orElseThrow(RUNTIME_EXCEPTION_SUPPLIER);

        // then
        Assertions.assertAll(
            () -> assertThat(gameEntity.getState()).isEqualTo("RUNNING"),
            () -> assertThat(gameEntity.getTurnColor()).isEqualTo("WHITE")
        );


    }

    @Test
    @DisplayName("방 ID로 게임을 찾는다.")
    public void findGameByRoomName() {
        // given
        final Optional<GameEntity> optionalEntity = dao.findGameByRoomId(roomId);
        // when
        final boolean isPresent = optionalEntity.isPresent();
        // then
        assertThat(isPresent).isTrue();
    }


    @AfterEach
    void setDown() {
        roomDao.deleteRoom(roomId);
    }
}