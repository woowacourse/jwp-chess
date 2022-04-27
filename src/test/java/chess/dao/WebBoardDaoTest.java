package chess.dao;

import chess.domain.game.ChessBoard;
import chess.domain.pieces.Color;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@JdbcTest
@Sql({"schema.sql"})
class WebBoardDaoTest {

    private final BoardDao<ChessBoard> dao;

    int boardId;

    @Autowired
    WebBoardDaoTest(NamedParameterJdbcTemplate jdbcTemplate) {
        dao = new WebChessBoardDao(new WebChessMemberDao(jdbcTemplate), jdbcTemplate);
    }

    @BeforeEach
    void setup() {
        final ChessBoard board = dao.save(new ChessBoard("개초보만", "1234"));
        this.boardId = board.getId();
    }

    @Test
    void saveTest() {
        final ChessBoard board = dao.save(new ChessBoard("개초보만", "1234"));
        assertThat(board.getRoomTitle()).isEqualTo("개초보만");
    }

    @Test
    void getByIdTest() {
        final ChessBoard board = dao.save(new ChessBoard("개초보만", "1234"));
        final ChessBoard foundBoard = dao.getById(board.getId());
        assertAll(
                () -> assertThat(foundBoard.getRoomTitle()).isEqualTo("개초보만"),
                () -> assertThat(foundBoard.getTurn()).isEqualTo(Color.WHITE)
        );
    }

    @Test
    void deleteBoard() {
        final ChessBoard board = dao.save(new ChessBoard("aaa", "1234"));
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
        List<ChessBoard> boards = dao.findAll();

        assertThat(boards.size()).isEqualTo(0);
    }

    @Test
    void updateTurn() {
        dao.updateTurn(Color.BLACK, boardId);
        ChessBoard chessBoard = dao.getById(boardId);

        assertThat(chessBoard.getTurn()).isEqualTo(Color.BLACK);
    }

    @AfterEach
    void setDown() {
        dao.deleteAll();
    }
}
