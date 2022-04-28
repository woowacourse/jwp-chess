package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.entities.ChessGame;
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
        final ChessGame board = dao.save(new ChessGame("개초보만", "1111"));
        this.boardId = board.getId();
    }

    @Test
    void saveTest() {
        final ChessGame board = dao.save(new ChessGame("개초보만", "1111"));
        assertThat(board.getRoomTitle()).isEqualTo("개초보만");
    }

    @Test
    void getByIdTest() {
        final ChessGame board = dao.save(new ChessGame("개초보만", Color.WHITE, "2222"));
        final ChessGame foundBoard = dao.getById(board.getId());
        assertAll(
                () -> assertThat(foundBoard.getRoomTitle()).isEqualTo("개초보만"),
                () -> assertThat(foundBoard.getTurn()).isEqualTo(Color.WHITE),
                () -> assertThat(foundBoard.getPassword()).isEqualTo("2222")
        );
    }

    @Test
    void deleteBoard() {
        final ChessGame board = dao.save(new ChessGame("aaa", "1111"));
        int affectedRow = dao.deleteByIdAndPassword(board.getId(), board.getPassword());
        assertThat(affectedRow).isEqualTo(1);
    }

    @Test
    void findAllTest() {
        assertThat(dao.findAll().size()).isEqualTo(1);
    }

    @Test
    void deleteAll() {
        dao.deleteAll();
        List<ChessGame> boards = dao.findAll();

        assertThat(boards.size()).isEqualTo(0);
    }

    @Test
    void updateTurn() {
        dao.updateTurn(Color.BLACK, boardId);
        ChessGame chessBoard = dao.getById(boardId);

        assertThat(chessBoard.getTurn()).isEqualTo(Color.BLACK);
    }

    @AfterEach
    void setDown() {
        dao.deleteAll();
    }
}
