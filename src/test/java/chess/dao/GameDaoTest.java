package chess.dao;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class GameDaoTest {
    private GameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao(jdbcTemplate);

        jdbcTemplate.execute("drop table game if exists");
        jdbcTemplate.execute("create table game("
                + "game_id int primary key not null, current_turn varchar(10) default'white')");

        jdbcTemplate.update("insert into game(game_id, current_turn) values (?,?)", 0, "WHITE");
    }

    @Test
    void updateTurnTest() {
        int gameId = 0;
        gameDao.updateTurn(gameId, "BLACK");
        assertThat(gameDao.findTurnById(gameId)).isEqualTo("BLACK");
    }
}
