package chess.dao.jdbctemplate;

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
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        jdbcTemplateGameStatusDao = new JdbcTemplateGameStatusDao(jdbcTemplate);
        jdbcTemplateGameStatusDao.init(GameStatus.READY);
    }

    @DisplayName("체스게임을 시작하면 처음 상태는 READY 여야 한다.")
    @Test
    void getStatus() {
        // given
        GameStatus initStatus = GameStatus.READY;

        // then
        Assertions.assertThat(jdbcTemplateGameStatusDao.getStatus()).isEqualTo(initStatus.toString());
    }

    @DisplayName("상태를 PLAYING 로 변경 후 변경 값을 확인한다.")
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

    @DisplayName("리셋을 하면 초기값은 READY 이다.")
    @Test
    void reset() {
        // given
        GameStatus initStatus = GameStatus.READY;
        GameStatus nextStatus = GameStatus.PLAYING;
        //when
        jdbcTemplateGameStatusDao.update(initStatus.toString(), nextStatus.toString());
        jdbcTemplateGameStatusDao.reset(initStatus);
        // then
        Assertions.assertThat(jdbcTemplateGameStatusDao.getStatus()).isEqualTo(initStatus.toString());
    }
}
