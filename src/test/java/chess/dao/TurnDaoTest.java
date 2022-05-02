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
class TurnDaoTest extends DaoTest {

    private TurnDao turnDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        turnDao = new JdbcTurnDao(jdbcTemplate);

        super.setUp();
        jdbcTemplate.execute("CREATE TABLE turn(id long not null auto_increment primary key,\n" +
                "                roomId long not null,\n" +
                "                team varchar(5) not null,\n" +
                "                constraint fk_roomId foreign key (roomId) references room (id) on delete cascade)"
        );
        jdbcTemplate.execute("insert into room (name, password) values ('chessRoom', 'abcd')");
        jdbcTemplate.execute("insert into turn (roomId, team) values (1, 'WHITE')");
    }

    @Test
    @DisplayName("db에서 현재 턴을 찾는다.")
    void findTurn() {
        final TurnDto turnDto = turnDao.findTurn(1L);
        final String expected = "WHITE";

        final String actual = turnDto.getTurn();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("db에서 턴을 업데이트해준다.")
    void updateTurn() {
        turnDao.updateTurn(1L, "WHITE");
        final String expected = "BLACK";

        final String actual = turnDao.findTurn(1L).getTurn();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("db에서 턴을 화이트로 리셋해준다.")
    void resetTurn() {
        turnDao.updateTurn(1L, "WHITE");
        turnDao.resetTurn(1L);
        final String expected = "WHITE";

        final String actual = turnDao.findTurn(1L).getTurn();

        assertThat(actual).isEqualTo(expected);
    }
}