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
class StateDaoImplTest {

    @Autowired
    private StateDaoImpl2 stateDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        stateDao = new StateDaoImpl2(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE IF EXISTS state");
        jdbcTemplate.execute("CREATE TABLE state("
                + "  `name` VARCHAR(20) NOT NULL, "
                + "  PRIMARY KEY (name) "
                + ");");
    }

    @DisplayName("데이터를 삽입한다.")
    @Test
    void insert() {
        State state = new BlackTurn(Board.init());
        stateDao.insert(state);

        assertThat(stateDao.find(Board.init()).isBlackTurn()).isTrue();
    }

    @DisplayName("데이터를 삭제한다.")
    @Test
    void delete() {
        State state = new BlackTurn(Board.init());
        stateDao.insert(state);

        assertThat(stateDao.delete()).isEqualTo(1);
    }

    @DisplayName("데이터를 업데이트한다.")
    @Test
    void update() {
        State nowState = new BlackTurn(Board.init());
        stateDao.insert(nowState);

        State nextState = new WhiteTurn(Board.init());
        stateDao.update(nowState, nextState);

        assertThat(stateDao.find(Board.init()).isWhiteTurn()).isTrue();
    }
}
