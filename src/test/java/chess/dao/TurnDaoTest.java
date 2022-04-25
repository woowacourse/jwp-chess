package chess.dao;

import chess.dto.TurnDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class TurnDaoTest {

    private TurnDao turnDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        turnDao = new JdbcTurnDao(jdbcTemplate);

        jdbcTemplate.execute("drop table turn if exists");
        jdbcTemplate.execute("CREATE TABLE turn (team varchar(5) not null primary key)");
        jdbcTemplate.execute("insert into turn (team) values ('WHITE')");
    }

    @Test
    @DisplayName("db에서 현재 턴을 찾는다.")
    void findTurn() {
        final TurnDto turnDto = turnDao.findTurn();
        final String expected = "WHITE";

        final String actual = turnDto.getTurn();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("db에서 턴을 업데이트해준다.")
    void updateTurn() {
        turnDao.updateTurn("WHITE");
        final String expected = "BLACK";

        final String actual = turnDao.findTurn().getTurn();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("db에서 턴을 화이트로 리셋해준다.")
    void resetTurn() {
        turnDao.updateTurn("WHITE");
        turnDao.resetTurn();
        final String expected = "WHITE";

        final String actual = turnDao.findTurn().getTurn();

        assertThat(actual).isEqualTo(expected);
    }
}