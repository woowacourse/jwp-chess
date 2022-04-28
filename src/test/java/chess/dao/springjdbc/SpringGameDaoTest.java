package chess.dao.springjdbc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dao.GameEntity;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
@Sql("classpath:schema-test.sql")
class SpringGameDaoTest {

    private SpringGameDao springGameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        springGameDao = new SpringGameDao(jdbcTemplate);
    }

    @Test
    @DisplayName("게임의 id를 통해 게임 조회가 정상적인지 확인")
    void findById() {
        GameFactory.setUpGames(jdbcTemplate, "first");
        GameEntity foundGame = springGameDao.findById(1);
        assertThat(foundGame.getName()).isEqualTo("first");
    }

    @Test
    @DisplayName("게임의 상태와 턴을 업데이트 하는지 확인")
    void update() {
        GameFactory.setUpGames(jdbcTemplate, "first");
        int affectedRows = springGameDao.update(new GameEntity(1, "first", "READY", "BLACK", "password"));
        GameEntity foundGame = springGameDao.findById(1);
        assertAll(() -> {
            assertThat(affectedRows).isEqualTo(1);
            assertThat(foundGame.getTurn()).isEqualTo("BLACK");
            assertThat(foundGame.getStatus()).isEqualTo("READY");
        });
    }

    @Test
    @DisplayName("저장된 모든 게임이 조회되는지 확인")
    void findAll() {
        GameFactory.setUpGames(jdbcTemplate, "first", "second");
        List<GameEntity> games = springGameDao.findAll();
        assertThat(games).hasSize(2);
    }

    @Test
    @DisplayName("이름과 암호를 받아 게임을 만든 후 id값을 적절하게 반환하는지 확인")
    void createGame() {
        int createdGameId = springGameDao.createGame("testGameName", "testPassword");
        assertThat(createdGameId).isEqualTo(1);
    }

    @Test
    @DisplayName("게임 id를 받아 해당 게임을 삭제")
    void deleteGame() {
        GameFactory.setUpGames(jdbcTemplate, "first", "second");
        int affectedRows = springGameDao.deleteGame(1);
        int remainGames = springGameDao.findAll().size();
        assertAll(() -> {
            assertThat(affectedRows).isOne();
            assertThat(remainGames).isOne();
        });
    }

    @Test
    @DisplayName("getPassword: 게임 id에 해당하는 비밀번호를 가져온다.")
    void getPassword() {
        GameFactory.setUpGames(jdbcTemplate, "first");
        String password = springGameDao.findPasswordById(1);
        assertThat(password).isEqualTo("password");
    }
}
