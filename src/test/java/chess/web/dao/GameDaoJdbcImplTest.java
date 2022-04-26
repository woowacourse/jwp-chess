package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.state.StateType;
import chess.web.dto.GameDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("classpath:init.sql")
class GameDaoJdbcImplTest {

    private GameDao gameDao;
    private static final int gameId = 1;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDaoJdbcImpl(jdbcTemplate);

        gameDao.save("체스게임방", "1234", StateType.WHITE_TURN);
    }

    @Test
    void updateStateById() {
        StateType expected = StateType.BLACK_TURN;
        gameDao.updateStateById(gameId, expected);

        StateType actual = gameDao.findStateById(gameId);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findStateById() {
        StateType actual = gameDao.findStateById(gameId);
        StateType expected = StateType.WHITE_TURN;

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findPasswordById() {
        String actual = gameDao.findPasswordById(gameId);
        String expected = "1234";

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void findAll() {
        List<GameDto> gameDtos = gameDao.findAll();
        int actual = gameDtos.size();
        int expected = 1;

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void deleteGameById() {
       gameDao.deleteGameById(gameId);

        int actual = gameDao.findAll().size();
        int expected = 0;

        assertThat(actual).isEqualTo(expected);
    }
}
