package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class JdbcTemplateTurnDaoTest {

    private JdbcTemplateTurnDao jdbcTemplateTurnDao;
    private int id;

    @Autowired
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @BeforeEach
    void setUp() {
        JdbcTemplateGameDao jdbcTemplateGameDao = new JdbcTemplateGameDao(jdbcTemplate, new GameMapper());
        id = jdbcTemplateGameDao.create("asdf", "1234");
        jdbcTemplateTurnDao = new JdbcTemplateTurnDao(jdbcTemplate);
        jdbcTemplateTurnDao.init(id);
    }


    @Test
    @DisplayName("초기 값을 확인한다.")
    void getTurn() {
        assertThat(jdbcTemplateTurnDao.getTurn(id)).isEqualTo("white");
    }

    @Test
    @DisplayName("차례를 변경 후, 변경 값을 확인한다.")
    void update() {
        jdbcTemplateTurnDao.update("white", "black");
        assertThat(jdbcTemplateTurnDao.getTurn(id)).isEqualTo("black");
    }

    @Test
    @DisplayName("리셋을 확인한다.")
    void reset() {
        jdbcTemplateTurnDao.reset(id);
        assertThat(jdbcTemplateTurnDao.getTurn(id)).isEqualTo("white");
    }
}
