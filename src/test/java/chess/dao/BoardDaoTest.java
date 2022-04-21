package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.domain.game.ChessBoard;
import chess.domain.pieces.Color;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class BoardDaoTest {

    private final BoardDao<ChessBoard> dao = new ChessBoardDao(new ChessConnectionManager());

    @Test
    void saveTest() {
        final ChessBoard board = dao.save(new ChessBoard("개초보만"));
        assertThat(board.getRoomTitle()).isEqualTo("개초보만");
        dao.deleteById(board.getId());
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
        assertThat(dao.findAll().size()).isEqualTo(2);
    }

    @AfterEach
    void setDown() {
        dao.deleteAll();
    }
}
