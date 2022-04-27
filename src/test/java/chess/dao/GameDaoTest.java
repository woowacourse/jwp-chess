package chess.dao;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class GameDaoTest {
    private GameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao(jdbcTemplate);

        jdbcTemplate.execute("create table game("
                + "game_id int primary key not null, current_turn varchar(10) default'WHITE')");

        jdbcTemplate.update("insert into game(game_id, current_turn) values (?,?)", 0, "WHITE");
    }

    @AfterEach
    void clean() {
        jdbcTemplate.execute("drop table game if exists");
    }

    @Test
    void updateTurnTest() {
        int gameId = 0;
        gameDao.updateTurn(gameId, "BLACK");
        assertThat(gameDao.findTurnById(gameId)).isEqualTo("BLACK");
    }

    @Test
    void findTurnByIdTest() {
        int gameId = 0;
        String turn = gameDao.findTurnById(gameId);
        assertThat(turn).isEqualTo("WHITE");
    }

    @Test
    void insertGameTest() {
        int gameId = 1;
        gameDao.insertGame(gameId, "WHITE");
        String turn = gameDao.findTurnById(gameId);
        assertThat(turn).isEqualTo("WHITE");
    }

    @Test
    void deleteGameTest() {
        int gameId = 0;
        gameDao.deleteGame(gameId);
        assertThatThrownBy(() -> gameDao.findTurnById(gameId))
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
