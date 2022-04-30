package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.player.Team;
import chess.dto.TurnDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("classpath:turnInit.sql")
class TurnDaoTest {

    private TurnDao turnDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        turnDao = new JdbcTurnDao(jdbcTemplate);
        turnDao.insertTurn(1, Team.WHITE.getName());
    }

    @Test
    @DisplayName("db에서 현재 턴을 찾는다.")
    void findTurn() {

        final TurnDto turnDto = turnDao.findTurn(1);
        final String expected = "WHITE";

        final String actual = turnDto.getTurn();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("db에서 턴을 업데이트해준다.")
    void updateTurn() {
        turnDao.updateTurn(1, "BLACK", "WHITE");
        final String expected = "BLACK";

        final String actual = turnDao.findTurn(1).getTurn();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("db에서 턴을 화이트로 리셋해준다.")
    void resetTurn() {
        turnDao.initializeTurn(1, Team.WHITE.getName());
        final String expected = "WHITE";

        final String actual = turnDao.findTurn(1).getTurn();

        assertThat(actual).isEqualTo(expected);
    }


}