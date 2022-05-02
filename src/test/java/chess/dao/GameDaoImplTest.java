package chess.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class GameDaoImplTest {
    private static final String TEST_GAME_NAME = "test";
    private static final String TEST_GAME_PASSWORD = "password";

    private GameDaoImpl gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDaoImpl(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE game IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE game("
            + "id   INT NOT NULL AUTO_INCREMENT,"
            + "turn ENUM('WHITE', 'BLACK'),"
            + "name VARCHAR(10) NOT NULL,"
            + "password VARCHAR(10) NOT NULL,"
            + "PRIMARY KEY (id))"
        );
    }

    @DisplayName("새로운 게임을 game 테이블에 생성한다.")
    @Test
    void createGame() {
        System.out.println(gameDao.createGameAndGetId(TEST_GAME_NAME, TEST_GAME_PASSWORD));
    }

    @DisplayName("게임을 game 테이블로부터 제거한다.")
    @Test
    void deleteGame() {
        // given & when
        int id = gameDao.createGameAndGetId(TEST_GAME_NAME, TEST_GAME_PASSWORD);

        // then
        gameDao.deleteGame(id);
    }

    @DisplayName("게임의 턴을 흰색으로 변경한다.")
    @Test
    void updateTurnToWhite() {
        // given & when
        int id = gameDao.createGameAndGetId(TEST_GAME_NAME, TEST_GAME_PASSWORD);

        // then
        gameDao.updateTurnToWhite(id);
    }

    @DisplayName("게임의 턴을 검정색으로 변경한다.")
    @Test
    void updateTurnToBlack() {
        // given & when
        int id = gameDao.createGameAndGetId(TEST_GAME_NAME, TEST_GAME_PASSWORD);

        // then
        gameDao.updateTurnToBlack(id);
    }
}
