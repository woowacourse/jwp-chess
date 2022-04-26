package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.board.Board;
import chess.board.Team;
import chess.board.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
class BoardDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private BoardDao boardDao;

    @BeforeEach
    void setUp() {
        boardDao = new BoardDaoImpl(jdbcTemplate);
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("처음 저장된 board가 초기화된 Turn과 동일한지 테스트")
    void findTurnById() {
        Long boardId = boardDao.save(Turn.init());
        //when
        Turn turn = boardDao.findTurnById(boardId).get();
        //then
        assertThat(turn).isEqualTo(Turn.init());
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("현재 턴을 black으로 업데이트하면 DB에 반영이 되는지 테스트")
    void updateTurnById() {
        Long boardId = boardDao.save(Turn.init());
        //when
        boardDao.update(boardId, new Turn(Team.BLACK));
        Turn turn = boardDao.findTurnById(boardId).get();
        //then
        assertThat(turn).isEqualTo(new Turn(Team.BLACK));
    }
}
