package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.jdbctemplate.JdbcTemplateTurnDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class JdbcTemplateTurnDaoTest {

    private JdbcTemplateTurnDao jdbcTemplateTurnDao;

    @Autowired
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @BeforeEach
    void setUp() {
        jdbcTemplateTurnDao = new JdbcTemplateTurnDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE turn IF EXISTS");

        jdbcTemplate.execute("create table turn("
                + " team varchar(10) not null,"
                + " primary key (team)"
                + ")");

        jdbcTemplate.update("INSERT INTO turn(team) VALUES (?)", "white");
    }

    @Test
    @DisplayName("초기 값을 확인한다.")
    void getTurn() {
        assertThat(jdbcTemplateTurnDao.getTurn()).isEqualTo("white");
    }

    @Test
    @DisplayName("차례를 변경 후, 변경 값을 확인한다.")
    void update() {
        jdbcTemplateTurnDao.update("white", "black");
        assertThat(jdbcTemplateTurnDao.getTurn()).isEqualTo("black");
    }

    @Test
    @DisplayName("리셋을 확인한다.")
    void reset() {
        jdbcTemplateTurnDao.reset();
        assertThat(jdbcTemplateTurnDao.getTurn()).isEqualTo("white");
    }
}
