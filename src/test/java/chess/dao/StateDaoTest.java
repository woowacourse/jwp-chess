package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.model.board.Board;
import chess.model.state.State;
import chess.model.state.running.BlackTurn;
import chess.model.state.running.WhiteTurn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class StateDaoTest {

    @Autowired
    private StateDao stateDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        stateDao = new StateDao(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE IF EXISTS state");
        jdbcTemplate.execute("CREATE TABLE state("
                + " `id` VARCHAR(2) NOT NULL, "
                + "  `name` VARCHAR(20) NOT NULL, "
                + "  PRIMARY KEY (`id`) "
                + ");");

        jdbcTemplate.update("insert into state(id, name) values (?, ?)", "1", "BLACK_TURN");
    }

    @DisplayName("데이터를 삽입한다.")
    @Test
    void insert() {
        String id = "2";
        State state = new BlackTurn(Board.init());

        assertThat(stateDao.insert(id, state)).isEqualTo(1);
    }

    @DisplayName("데이터를 삭제한다.")
    @Test
    void delete() {
        String id = "1";
        assertThat(stateDao.deleteFrom(id)).isEqualTo(1);
    }

    @DisplayName("데이터를 업데이트한다.")
    @Test
    void update() {
        String id = "1";
        State nowState = new BlackTurn(Board.init());
        State nextState = new WhiteTurn(Board.init());
        stateDao.update(id, nowState, nextState);

        assertThat(stateDao.find(id, Board.init()).isWhiteTurn()).isTrue();
    }
}
