package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.board.BoardEntity;
import chess.board.Turn;
import java.util.Optional;
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
    @DisplayName("체스방을 저장한다.")
    void save() {
        Turn turn = Turn.init();
        String title = "title";
        String password = "password";

        Long id = boardDao.save(turn.getTeam().value(), title, password);

        Optional<BoardEntity> board = boardDao.findById(id);
        assertThat(board).isPresent();
        assertThat(board.get().getTitle()).isEqualTo(title);
        assertThat(board.get().getPassword()).isEqualTo(password);
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("id에 맞는 체스 게임의 순서를 수정한다.")
    void updateTurnById() {
        Turn turn = Turn.init();
        String title = "title";
        String password = "password";
        Long id = boardDao.save(turn.getTeam().value(), title, password);
        Turn changedTurn = turn.change();

        boardDao.updateTurnById(id, changedTurn.getTeam().value());

        Optional<BoardEntity> board = boardDao.findById(id);
        assertThat(board).isPresent();
        assertThat(board.get().getTurn()).isEqualTo(changedTurn.getTeam().value());
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("id에 맞는 체스방의 정보를 반환한다.")
    void findById() {
        Turn turn = Turn.init();
        String title = "title";
        String password = "password";
        Long id = boardDao.save(turn.getTeam().value(), title, password);

        Optional<BoardEntity> board = boardDao.findById(id);

        assertThat(board).isPresent();
        assertThat(board.get().getTitle()).isEqualTo(title);
        assertThat(board.get().getPassword()).isEqualTo(password);
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("id와 패스워드가 맞다면 체스방을 제거한다.")
    void delete() {
        Turn turn = Turn.init();
        String title = "title";
        String password = "password";
        Long id = boardDao.save(turn.getTeam().value(), title, password);

        boardDao.delete(id, password);

        Optional<BoardEntity> board = boardDao.findById(id);
        assertThat(board).isNotPresent();
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("비밀번호가 틀리면 체스방을 제거할 수 없다.")
    void deleteWrongPassword() {
        Turn turn = Turn.init();
        String title = "title";
        String password = "password";
        Long id = boardDao.save(turn.getTeam().value(), title, password);

        boardDao.delete(id, "test");

        Optional<BoardEntity> board = boardDao.findById(id);
        assertThat(board).isPresent();
        assertThat(board.get().getTitle()).isEqualTo(title);
        assertThat(board.get().getPassword()).isEqualTo(password);
    }
}
