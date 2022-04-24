package chess.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import chess.domain.piece.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class GameJdbcDaoTest {

    private GameJdbcDao gameJdbcDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameJdbcDao = new GameJdbcDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE game IF EXISTS");
        jdbcTemplate.execute("create table game("
            + "id varchar(100) not null unique, "
            + "turn varchar(10) not null, "
            + "force_end_flag tinyint(1) not null default false"
            + ")");
    }

    @Test
    void createById() {
        gameJdbcDao.createById("1234");
    }

    @Test
    void isInId() {
        gameJdbcDao.createById("1234");
        assertThat(gameJdbcDao.isInId("1234")).isTrue();
        assertThat(gameJdbcDao.isInId("123")).isFalse();
    }

    @Test
    void findForceEndFlagById() {
        gameJdbcDao.createById("1234");
        assertThat(gameJdbcDao.findForceEndFlagById("1234")).isFalse();
        assertThatThrownBy(() -> gameJdbcDao.findForceEndFlagById("124"))
            .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void findTurnById() {
        gameJdbcDao.createById("1234");
        assertThat(gameJdbcDao.findTurnById("1234")).isEqualTo(Color.BLACK);
        assertThatThrownBy(() -> gameJdbcDao.findTurnById("124"))
            .isInstanceOf(EmptyResultDataAccessException.class);
    }

    @Test
    void updateTurnById() {
        gameJdbcDao.createById("1234");

        gameJdbcDao.updateTurnById(Color.WHITE, "1234");

        assertThat(gameJdbcDao.findTurnById("1234")).isEqualTo(Color.WHITE);
    }

    @Test
    void updateForceEndFlagById() {
        gameJdbcDao.createById("1234");

        gameJdbcDao.updateForceEndFlagById(true, "1234");

        assertThat(gameJdbcDao.findForceEndFlagById("1234")).isEqualTo(true);
    }

    @Test
    void deleteById() {
        gameJdbcDao.createById("1234");
        assertThat(gameJdbcDao.isInId("1234")).isTrue();

        gameJdbcDao.deleteById("1234");

        assertThat(gameJdbcDao.isInId("1234")).isFalse();
    }

}