package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.state.StateType;
import chess.web.dto.game.TitleDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("classpath:init.sql")
class GameDaoTest {

    private static final int GAME_ID = 1;

    private GameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDaoJdbcImpl(jdbcTemplate);

        gameDao.save("체스게임방", "1234", StateType.WHITE_TURN);
    }

    @DisplayName("게임 상태를 변경한다.")
    @Test
    void updateStateById() {
        StateType expected = StateType.BLACK_TURN;
        gameDao.updateStateById(GAME_ID, expected);

        StateType actual = gameDao.findStateById(GAME_ID);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("게임 상태를 가져온다.")
    @Test
    void findStateById() {
        StateType actual = gameDao.findStateById(GAME_ID);
        StateType expected = StateType.WHITE_TURN;

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("게임의 비밀번호를 가져온다.")
    @Test
    void findPasswordById() {
        String actual = gameDao.findPasswordById(GAME_ID);
        String expected = "1234";

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("게임을 전부 가져온다.")
    @Test
    void findAll() {
        List<TitleDto> gameDtos = gameDao.findAll();
        int actual = gameDtos.size();
        int expected = 1;

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("게임을 삭제한다.")
    @Test
    void deleteGameById() {
        gameDao.deleteGameById(GAME_ID);

        int actual = gameDao.findAll().size();
        int expected = 0;

        assertThat(actual).isEqualTo(expected);
    }
}
