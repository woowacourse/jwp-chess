package chess.model.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("/initGames.sql")
class GameDaoTest {

    private static final Long firstGeneratedKey = 1L;

    private final GameDao gameDao;

    @Autowired
    GameDaoTest(JdbcTemplate jdbcTemplate) {
        gameDao = new GameDao(jdbcTemplate); // @JdbcTest는 일반적인 @ConfigurationProperties와 @Component 빈들은 스캔되지 않는다.
    }

    @Test
    void saveGame() {
        Long gameId = gameDao.saveGame("saveGameTest", "pw1234");
        List<Long> games = gameDao.findAllGameId();

        assertThat(games).contains(gameId, (long) (games.size() - 1));
    }

    @Test
    void findTurnByGameId() {
        Optional<String> turn = gameDao.findTurnByGameId(firstGeneratedKey);

        assertThat(turn.get()).isEqualTo("white");
    }

    @Test
    void updateTurnByGameId() {
        gameDao.updateTurnByGameId(firstGeneratedKey, "white");

        assertThat(gameDao.findTurnByGameId(firstGeneratedKey).get()).isEqualTo("white");
    }

    @Test
    void deleteByGameId() {
        gameDao.deleteByGameId(firstGeneratedKey);

        assertThat(gameDao.findAllGameId()).doesNotContain(firstGeneratedKey);
    }

    @Test
    void findTitleByGameId() {
        String password = gameDao.findTitleByGameId(firstGeneratedKey);

        assertThat(password).isEqualTo("title1");
    }

    @Test
    void findPasswordByGameId() {
        String password = gameDao.findPasswordByGameId(firstGeneratedKey);

        assertThat(password).isEqualTo("pw1234");
    }
}
