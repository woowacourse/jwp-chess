package chess.dao;

import static org.assertj.core.api.Assertions.*;

import chess.entity.GameEntity;
import org.junit.jupiter.api.AfterEach;
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

        jdbcTemplate.execute("create table game("
                + "game_id int primary key auto_increment,"
                + " current_turn varchar(10) default'WHITE')");

        gameDao.insert(new GameEntity("WHITE"));
    }

    @AfterEach
    void clean() {
        gameDao.deleteById(1);
        jdbcTemplate.execute("drop table game if exists");
    }

    @Test
    void updateTurnTest() {
        int gameId = 1;
        gameDao.updateById(new GameEntity(gameId, "BLACK"));
        assertThat(gameDao.findById(gameId).getTurn()).isEqualTo("BLACK");
    }

    @Test
    void findTurnByIdTest() {
        int gameId = 1;
        String turn = gameDao.findById(gameId).getTurn();
        assertThat(turn).isEqualTo("WHITE");
    }

    @Test
    void insertWithKeyHolderTest() {
        int id = gameDao.insertWithKeyHolder(new GameEntity("WHITE"));
        assertThat(id).isEqualTo(2);
    }
}
