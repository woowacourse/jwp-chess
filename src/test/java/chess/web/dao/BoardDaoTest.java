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
    private Long boardId = 1L;

    @BeforeEach
    void setUp() {
        boardDao = new BoardDaoImpl(jdbcTemplate);
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("처음 저장된 board가 초기화된 Turn과 동일한지 테스트")
    void findTurnById() {
        //when
        Turn turn = boardDao.findTurnById(boardId).get();
        //then
        assertThat(turn).isEqualTo(Turn.init());
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("현재 턴을 black으로 업데이트하면 DB에 반영이 되는지 테스트")
    void updateTurnById() {
        //when
        boardDao.updateTurnById(boardId, "black");
        Turn turn = boardDao.findTurnById(boardId).get();
        //then
        assertThat(turn).isEqualTo(new Turn(Team.BLACK));
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("새로운 보드판이 만들어지면 pk값이 1 증가한다.")
    void save() {
        //when
        Long saveId = boardDao.save(boardId + 1, Turn.init());
        //then
        assertThat(boardId + 1).isEqualTo(saveId);
        boardDao.deleteById(saveId);
    }
}
