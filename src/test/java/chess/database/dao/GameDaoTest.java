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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import chess.database.dao.spring.SpringGameDao;
import chess.database.dto.GameStateDto;
import chess.domain.game.GameState;
import chess.domain.game.Ready;

@JdbcTest
@Sql("classpath:ddl.sql")
class GameDaoTest {

    private static final String TEST_ROOM_NAME = "TEST_ROOM_NAME";
    private static final String TEST_CREATION_ROOM_NAME = "TEST_CREATION_ROOM_NAME";
    private static final Supplier<RuntimeException> RUNTIME_EXCEPTION_SUPPLIER =
        () -> new RuntimeException("[ERROR] 방이 존재하지 않습니다.");

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private GameDao dao;
    private Long testId;

    @BeforeEach
    void setUp() {
        dao = new SpringGameDao(dataSource, jdbcTemplate);
        GameState state = new Ready();
        testId = dao.saveGame(GameStateDto.of(state), TEST_ROOM_NAME, "password");
    }

    @Test
    @DisplayName("게임을 생성한다.")
    public void createGame() {
        // given
        GameState state = new Ready();

        // when
        final Long savedId = dao.saveGame(GameStateDto.of(state), TEST_CREATION_ROOM_NAME, "password");

        // then
        assertThat(savedId).isNotNull();
    }

    @Test
    @DisplayName("ID로 게임 상태와 턴 색깔을 조회한다.")
    public void insert() {
        // given
        final GameStateDto gameStateDto = dao.findGameById(testId)
            .orElseThrow(RUNTIME_EXCEPTION_SUPPLIER);
        // when
        // then
        Assertions.assertAll(
            () -> assertThat(gameStateDto.getState()).isEqualTo("READY"),
            () -> assertThat(gameStateDto.getTurnColor()).isEqualTo("WHITE")
        );
    }

    @Test
    @DisplayName("ID로 게임 상태와 턴 색깔을 수정한다.")
    public void update() {
        // given
        GameState state = new Ready();
        GameState started = state.start();
        // when
        dao.updateState(GameStateDto.of(started), testId);
        final GameStateDto gameStateDto = dao.findGameById(testId).orElseThrow(RUNTIME_EXCEPTION_SUPPLIER);

        // then
        assertThat(gameStateDto.getState()).isEqualTo("RUNNING");
        assertThat(gameStateDto.getTurnColor()).isEqualTo("WHITE");
    }

    @Test
    @DisplayName("게임을 삭제한다.")
    public void delete() {
        assertThatCode(() -> dao.removeGame(testId)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("게임 아이디와 이름을 읽는다.")
    public void readGameRoomIdAndNames() {
        // given & when
        final Map<Long, String> idAndNames = dao.readGameRoomIdAndNames();
        // then
        assertThat(idAndNames).containsExactly(Map.entry(testId, TEST_ROOM_NAME));
    }

    @Test
    @DisplayName("방 이름으로 게임을 찾는다.")
    public void findGameByRoomName() {
        // given
        final Optional<GameStateDto> foundGameStateDto = dao.findGameByRoomName(TEST_ROOM_NAME);
        // when
        final boolean isPresent = foundGameStateDto.isPresent();
        // then
        assertThat(isPresent).isTrue();
    }

    @Test
    @DisplayName("아이디로 비밀번호를 찾는다.")
    public void findPasswordById() {
        // given
        final Optional<String> foundPassword = dao.findPasswordById(testId);
        // when
        final boolean isPresent = foundPassword.isPresent();
        // then
        assertThat(isPresent).isTrue();
    }





    @AfterEach
    void setDown() {
        dao.removeGame(testId);
    }
}