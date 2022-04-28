package chess.web.dao;

import chess.domain.board.Board;
import chess.domain.board.Team;
import chess.domain.board.Turn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql("/data.sql")
class RoomDaoTest {

    @Autowired
    private RoomDao roomDao;
    private Long boardId;

    @BeforeEach
    void setUp() {

        boardId = roomDao.save("첫번째 방제목", "123");
    }

    @Test
    @DisplayName("처음 저장된 board가 초기화된 Turn과 동일한지 테스트")
    void findTurnById() {
        //when
        Turn turn = roomDao.findTurnById(boardId).get();
        //then
        assertThat(turn).isEqualTo(Turn.init());
    }

    @Test
    @DisplayName("현재 턴을 black으로 업데이트하면 DB에 반영이 되는지 테스트")
    void updateTurnById() {
        //when
        roomDao.updateTurnById(boardId, "black");
        Turn turn = roomDao.findTurnById(boardId).get();
        //then
        assertThat(turn).isEqualTo(new Turn(Team.BLACK));
    }

    @Test
    @DisplayName("새로운 보드판이 만들어지면 pk값이 1 증가한다.")
    void save() {
        //when
        Long saveId = roomDao.save("방제목","123");
        //then
        assertThat(boardId + 1).isEqualTo(saveId);
        roomDao.deleteById(saveId);

    }

    @Test
    @DisplayName("현재 board판만 있고 piece들은 없으므로 size가 0이어야 한다.")
    void findById() {
        Board board = roomDao.findById(boardId).get();
        assertThat(board.getPieces().getPieces().size()).isEqualTo(0);
    }
}
