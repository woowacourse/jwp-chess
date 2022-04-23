package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.game.ChessBoard;
import chess.domain.pieces.Color;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class WebBoardDaoTest {

    @Autowired
    private WebChessBoardDao dao;

    int boardId;

    @BeforeEach
    void setup() {
        final ChessBoard board = dao.save(new ChessBoard("개초보만"));
        this.boardId = board.getId();
    }

    @Test
    void saveTest() {
        final ChessBoard board = dao.save(new ChessBoard("개초보만"));
        assertThat(board.getRoomTitle()).isEqualTo("개초보만");
    }

    @Test
    void getByIdTest() {
        final ChessBoard board = dao.save(new ChessBoard("개초보만"));
        final ChessBoard foundBoard = dao.getById(board.getId());
        assertAll(
                () -> assertThat(foundBoard.getRoomTitle()).isEqualTo("개초보만"),
                () -> assertThat(foundBoard.getTurn()).isEqualTo(Color.WHITE)
        );
    }

    @Test
    void deleteBoard() {
        final ChessBoard board = dao.save(new ChessBoard("aaa"));
        int affectedRow = dao.deleteById(board.getId());
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
