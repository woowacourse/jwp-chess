package chess;

import static org.assertj.core.api.Assertions.assertThat;

import chess.model.dao.GameDao;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("/initGames.sql")
class GameDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private GameDao gameDao;

    @BeforeEach
    void initPieceDaoTest() {
        gameDao = new GameDao(jdbcTemplate); // @JdbcTest는 일반적인 @ConfigurationProperties와 @Component 빈들은 스캔되지 않는다.
    }

    @Test
    void saveGame() {
        Long gameId = gameDao.saveGame("title1", "pw1234");
        List<Long> games = gameDao.findAllGameId();

        assertThat(games).containsExactly(gameId);
    }

    @Test
    void findTurnByGameId() {
        Long gameId = gameDao.saveGame("title1", "pw1234");
        Optional<String> turn = gameDao.findTurnByGameId(gameId);

        assertThat(turn.get()).isEqualTo("white");
    }

    @Test
    void updateTurnByGameId() {
        Long gameId = gameDao.saveGame("title1", "pw1234");
        gameDao.updateTurnByGameId(gameId, "white");

        assertThat(gameDao.findTurnByGameId(gameId).get()).isEqualTo("white");
    }

    @Test
    void deleteByGameId() {
        Long gameId = gameDao.saveGame("title1", "pw1234");
        gameDao.deleteByGameId(gameId);

        assertThat(gameDao.findAllGameId()).doesNotContain(gameId);
    }

    @Test
    void findTitleByGameId() {
        Long gameId = gameDao.saveGame("title1", "pw1234");
        String password = gameDao.findTitleByGameId(gameId);

        assertThat(password).isEqualTo("title1");
    }

    @Test
    void findPasswordByGameId() {
        Long gameId = gameDao.saveGame("title1", "pw1234");
        String password = gameDao.findPasswordByGameId(gameId);

        assertThat(password).isEqualTo("pw1234");
    }
}
