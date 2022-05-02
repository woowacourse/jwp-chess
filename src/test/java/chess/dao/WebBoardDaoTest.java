package chess.dao;

import chess.domain.game.BoardEntity;
import chess.domain.pieces.Color;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
class WebBoardDaoTest {

    private final BoardDao<BoardEntity> dao;

    int boardId;

    @Autowired
    WebBoardDaoTest(NamedParameterJdbcTemplate jdbcTemplate) {
        dao = new WebChessBoardDao(new WebChessMemberDao(jdbcTemplate), jdbcTemplate);
    }

    @BeforeEach
    void setup() {
        final BoardEntity board = dao.save(new BoardEntity("개초보만", "1234"));
        this.boardId = board.getId();
    }

    @Test
    void saveTest() {
        final BoardEntity board = dao.save(new BoardEntity("개초보만", "1234"));
        assertThat(board.getRoomTitle()).isEqualTo("개초보만");
    }

    @Test
    void getByIdTest() {
        final BoardEntity board = dao.save(new BoardEntity("개초보만", "1234"));
        final BoardEntity foundBoard = dao.findById(board.getId()).get();
        assertAll(
                () -> assertThat(foundBoard.getRoomTitle()).isEqualTo("개초보만"),
                () -> assertThat(foundBoard.getTurn()).isEqualTo(Color.WHITE)
        );
    }

    @Test
    void deleteBoard() {
        final BoardEntity board = dao.save(new BoardEntity("aaa", "1234"));
        int affectedRow = dao.deleteByIdAndPassword(board.getId(), "1234");
        assertThat(affectedRow).isEqualTo(1);
    }

    @Test
    void findAllTest() {
        assertThat(dao.findAll().size()).isEqualTo(1);
    }

    @Test
    void deleteAll() {
        dao.deleteAll();
        List<BoardEntity> boards = dao.findAll();

        assertThat(boards.size()).isEqualTo(0);
    }

    @Test
    void updateTurn() {
        dao.updateTurn(Color.BLACK, boardId);
        BoardEntity chessBoard = dao.findById(boardId).get();

        assertThat(chessBoard.getTurn()).isEqualTo(Color.BLACK);
    }

    @AfterEach
    void setDown() {
        dao.deleteAll();
    }
}
