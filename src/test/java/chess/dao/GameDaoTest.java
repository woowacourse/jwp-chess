package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
        gameDao = new JdbcGameDao(jdbcTemplate);

        jdbcTemplate.execute("drop table game if exists");
        jdbcTemplate.execute("CREATE TABLE game\n"
                + "(\n"
                + "    roomId    int not null primary key,\n"
                + "    state varchar(3)\n"
                + ")\n");
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