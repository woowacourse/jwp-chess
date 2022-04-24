package chess.dao;

import chess.domain.game.GameId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class GameDaoImplTest {
    private static final GameId TEST_GAME_ID = GameId.from("TEST-GAME-ID");

    private GameDaoImpl gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDaoImpl(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE game IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE game("
            + "id   VARCHAR(36) NOT NULL,"
            + "turn ENUM('WHITE', 'BLACK'),"
            + "PRIMARY KEY (id))"
        );
    }

    @DisplayName("새로운 게임을 game 테이블에 생성한다.")
    @Test
    void createGame() {
        gameDao.createGame(TEST_GAME_ID);
    }

    @DisplayName("게임을 game 테이블로부터 제거한다.")
    @Test
    void deleteGame() {
        // given & when
        gameDao.createGame(TEST_GAME_ID);

        // then
        gameDao.deleteGame(TEST_GAME_ID);
    }

    @DisplayName("게임의 턴을 흰색으로 변경한다.")
    @Test
    void updateTurnToWhite() {
        // given & when
        gameDao.createGame(TEST_GAME_ID);

        // then
        gameDao.updateTurnToWhite(TEST_GAME_ID);
    }

    @DisplayName("게임의 턴을 검정색으로 변경한다.")
    @Test
    void updateTurnToBlack() {
        // given & when
        gameDao.createGame(TEST_GAME_ID);

        // then
        gameDao.updateTurnToBlack(TEST_GAME_ID);
    }
}
