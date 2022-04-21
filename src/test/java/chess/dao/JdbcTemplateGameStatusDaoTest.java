package chess.dao;

import chess.dao.jdbctemplate.JdbcTemplateGameStatusDao;
import chess.domain.GameStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class JdbcTemplateGameStatusDaoTest {

    private JdbcTemplateGameStatusDao jdbcTemplateGameStatusDao;

    @Autowired
    private JdbcTemplate jdbcTemplate = new JdbcTemplate();

    @BeforeEach
    void setUp() {
        jdbcTemplateGameStatusDao = new JdbcTemplateGameStatusDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE game_status IF EXISTS");

        jdbcTemplate.execute("create table game_status("
                + " status varchar(10) not null,"
                + " primary key (status)"
                + ")");

        jdbcTemplate.update("INSERT INTO game_status (status) values (?)", "READY");
    }

    @DisplayName("초기 값을 확인한다.")
    @Test
    void getStatus() {
        // given
        GameStatus initStatus = GameStatus.READY;

        // then
        Assertions.assertThat(jdbcTemplateGameStatusDao.getStatus()).isEqualTo(initStatus.toString());
    }

    @DisplayName("상태를 변경 후 변경 값을 확인한다.")
    @Test
    void update() {
        // given
        GameStatus initStatus = GameStatus.READY;
        GameStatus nextStatus = GameStatus.PLAYING;
        //when
        jdbcTemplateGameStatusDao.update(initStatus.toString(), nextStatus.toString());
        // then
        Assertions.assertThat(jdbcTemplateGameStatusDao.getStatus()).isEqualTo(nextStatus.toString());
    }

    @DisplayName("리셋을 확인한다.")
    @Test
    void reset() {
        // given
        GameStatus initStatus = GameStatus.READY;
        GameStatus nextStatus = GameStatus.PLAYING;
        //when
        jdbcTemplateGameStatusDao.update(initStatus.toString(), nextStatus.toString());
        jdbcTemplateGameStatusDao.reset();
        // then
        Assertions.assertThat(jdbcTemplateGameStatusDao.getStatus()).isEqualTo(initStatus.toString());
    }
}
