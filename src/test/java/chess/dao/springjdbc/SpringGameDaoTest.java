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
    @DisplayName("findById : 게임의 id를 통해 게임 조회가 정상적인지 확인")
    void findById() {
        GameFactory.setUpGames(jdbcTemplate, "first");
        GameEntity foundGame = springGameDao.findById(1);
        assertThat(foundGame.getName()).isEqualTo("first");
    }

    @Test
    @DisplayName("update : 게임의 상태와 턴을 업데이트 하는지 확인")
    void update() {
        GameFactory.setUpGames(jdbcTemplate, "first");
        springGameDao.update(new GameEntity(1, "first", "READY", "BLACK"));
        GameEntity foundGame = springGameDao.findById(1);
        assertAll(() -> {
            assertThat(foundGame.getTurn()).isEqualTo("BLACK");
            assertThat(foundGame.getStatus()).isEqualTo("READY");
        });
    }

    @Test
    @DisplayName("findAll: 저장된 모든 게임이 조회되는지 확인")
    void findAll() {
        GameFactory.setUpGames(jdbcTemplate, "first", "second");
        List<GameEntity> games = springGameDao.findAll();
        assertThat(games).hasSize(2);
    }

    @Test
    @DisplayName("createGame: 이름을 받아 게임을 만든 후 id값을 적절하게 반환하는지 확인")
    void createGame() {
        int createdGameId = springGameDao.createGame("testGameName");
        assertThat(createdGameId).isEqualTo(1);
    }
}
