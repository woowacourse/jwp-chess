package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("classpath:gameInit.sql")
class GameDaoTest {

    private GameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new JdbcGameDao(jdbcTemplate);
    }

    @Test
    @DisplayName("방 생성 시 run 상태로 만든다.")
    void makeRunState() {
        gameDao.startGame(1);
        assertThat(gameDao.getState(1)).isEqualTo("run");
    }

    @Test
    @DisplayName("방 종료 시 end 상태로 만든다.")
    void makeEndState() {
        gameDao.startGame(1);
        gameDao.endGame(1);
        assertThat(gameDao.getState(1)).isEqualTo("end");
    }
}