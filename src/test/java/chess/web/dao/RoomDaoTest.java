package chess.web.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.board.Room;
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
class RoomDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RoomDao roomDao;
    private BoardDao boardDao;

    @BeforeEach
    void setUp() {
        roomDao = new RoomDaoImpl(jdbcTemplate);
        boardDao = new BoardDaoImpl(jdbcTemplate);
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("체스방을 저장한다.")
    void save() {
        Long boardId = boardDao.save(Turn.init());
        String title = "title";
        String password = "password";

        roomDao.save(boardId, title, password);

        Optional<Room> optionalRoom = roomDao.findByBoardId(boardId);
        assertThat(optionalRoom).isPresent();
        assertThat(optionalRoom.get().getTitle()).isEqualTo(title);
        assertThat(optionalRoom.get().getPassword()).isEqualTo(password);
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("체스방을 찾는다.")
    void findByBoardId() {
        Long boardId = boardDao.save(Turn.init());
        String title = "title";
        String password = "password";
        Long id = roomDao.save(boardId, title, password);

        Optional<Room> optionalRoom = roomDao.findByBoardId(id);

        assertThat(optionalRoom).isPresent();
        assertThat(optionalRoom.get().getTitle()).isEqualTo(title);
        assertThat(optionalRoom.get().getPassword()).isEqualTo(password);
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("체스방을 제거한다.")
    void delete() {
        Long boardId = boardDao.save(Turn.init());
        String title = "title";
        String password = "password";
        Long id = roomDao.save(boardId, title, password);

        roomDao.delete(id, password);

        Optional<Room> optionalRoom = roomDao.findByBoardId(id);
        assertThat(optionalRoom).isNotPresent();
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("비밀번호가 틀리면 체스방을 제거할 수 없다.")
    void deleteWrongPassword() {
        Long boardId = boardDao.save(Turn.init());
        String title = "title";
        String password = "password";
        Long id = roomDao.save(boardId, title, password);

        roomDao.delete(id, "test");

        Optional<Room> optionalRoom = roomDao.findByBoardId(id);
        assertThat(optionalRoom).isPresent();
        assertThat(optionalRoom.get().getTitle()).isEqualTo(title);
        assertThat(optionalRoom.get().getPassword()).isEqualTo(password);
    }
}