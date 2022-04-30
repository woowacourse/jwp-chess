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
        gameDao.insertState(1);
    }

    @Test
    @DisplayName("방 생성 시 run 상태로 만든다.")
    void makeRunState() {
        assertThat(gameDao.getState(1)).isEqualTo("run");
    }

    @Test
    @DisplayName("방 종료 시 end 상태로 만든다.")
    void makeEndState() {
        gameDao.updateStateEnd(1);
        assertThat(gameDao.getState(1)).isEqualTo("end");
    }
}