package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.state.StateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class BoardStateDaoTest {

    private BoardStateDao boardStateDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        boardStateDao = new BoardStateDaoJdbcImpl(jdbcTemplate);

        jdbcTemplate.execute("drop table board if exists");
        jdbcTemplate.execute("create table board ("
                + " id int not null auto_increment primary key,"
                + " state varchar(10) not null)");

        boardStateDao.save(StateType.WHITE_TURN);
    }

    @DisplayName("보드 상태를 변경한다.")
    @Test
    void update() {
        boardStateDao.update(StateType.BLACK_TURN);
        StateType expected = StateType.BLACK_TURN;

        StateType actual = boardStateDao.selectState();
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("보드 상태를 가져온다.")
    @Test
    void selectState() {
        StateType actual = boardStateDao.selectState();
        StateType expected = StateType.WHITE_TURN;

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("보드 상태를 전부 삭제한다.")
    @Test
    void deleteAll() {
        boardStateDao.deleteAll();

        assertThatThrownBy(() -> boardStateDao.selectState())
                .isInstanceOf(EmptyResultDataAccessException.class);
    }
}
