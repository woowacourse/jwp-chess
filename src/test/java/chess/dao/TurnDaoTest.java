package chess.dao;

import chess.dto.TurnDto;
import chess.service.ChessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@Sql("classpath:turnInit.sql")
class TurnDaoTest {

    private TurnDao turnDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        turnDao = new JdbcTurnDao(jdbcTemplate);
        turnDao.initializeTurn(1);
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
        turnDao.updateTurn(1, "WHITE");
        final String expected = "BLACK";

        final String actual = turnDao.findTurn(1).getTurn();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("db에서 턴을 화이트로 리셋해준다.")
    void resetTurn() {
        turnDao.updateTurn(1, "WHITE");
        turnDao.initializeTurn(1);
        final String expected = "WHITE";

        final String actual = turnDao.findTurn(1).getTurn();

        assertThat(actual).isEqualTo(expected);
    }


}