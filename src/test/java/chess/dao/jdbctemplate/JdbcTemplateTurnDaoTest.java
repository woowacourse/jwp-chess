package chess.dao.jdbctemplate;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.piece.Team;
import javax.sql.DataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class JdbcTemplateTurnDaoTest {

    private JdbcTemplateTurnDao jdbcTemplateTurnDao;
    private JdbcTemplateRoomDao jdbcTemplateRoomDao;

    private int id;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    void setUp() {
        jdbcTemplateRoomDao = new JdbcTemplateRoomDao(dataSource);
        jdbcTemplateTurnDao = new JdbcTemplateTurnDao(jdbcTemplate);

        id = jdbcTemplateRoomDao.create("room1", "1111");
        jdbcTemplateTurnDao.create(Team.WHITE, id);
    }

    @AfterEach
    void clear() {
        jdbcTemplate.execute("delete from turn where id =" + id);
        jdbcTemplate.execute("delete from room where id =" + id);
    }

    @Test
    @DisplayName("초기 값은 white 이다.")
    void getTurn() {
        assertThat(jdbcTemplateTurnDao.getTurn(id)).isEqualTo("white");
    }

    @Test
    @DisplayName("차례를 변경 경하면 black 임을 확인한다.")
    void update() {
        jdbcTemplateTurnDao.update(Team.WHITE.toString(), Team.BLACK.toString(), id);
        assertThat(jdbcTemplateTurnDao.getTurn(id)).isEqualTo("black");
    }

    @Test
    @DisplayName("리셋하면 white 임을 확인한다.")
    void reset() {
        jdbcTemplateTurnDao.update(Team.WHITE.toString(), Team.BLACK.toString(), id);

        jdbcTemplateTurnDao.reset(Team.WHITE, id);
        assertThat(jdbcTemplateTurnDao.getTurn(id)).isEqualTo("white");
    }
}
