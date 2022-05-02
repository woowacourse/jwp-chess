package chess.dao;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import chess.model.Team;
import chess.model.board.Board;
import chess.model.piece.King;
import chess.model.piece.Pawn;
import chess.model.piece.Piece;
import chess.model.position.Position;

@JdbcTest
@Sql({"/db/schema.sql"})
public class SquareDaoTest {

    private SquareDao squareDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        squareDao = new SquareDao(jdbcTemplate);
    }

    @DisplayName("데이터를 삽입한다.")
    @Test
    void insert() {
        Position position = Position.from("a2");
        Piece piece = new Pawn(Team.WHITE);

        squareDao.insert(1L, position, piece);
        Board board = squareDao.createBoard(1L);

        assertThat(board.getBoard().size()).isEqualTo(2);
    }

    @DisplayName("데이터를 삭제한다.")
    @Test
    void delete() {
        assertThat(squareDao.delete(1L)).isEqualTo(1);
    }

    @DisplayName("데이터를 업데이트한다.")
    @Test
    void update() {
        Position position = Position.from("a1");
        Piece updatedPiece = new King(Team.WHITE);

        assertThat(squareDao.update(1L, position, updatedPiece)).isEqualTo(1);
    }

    @DisplayName("데이터를 모두 가져온다.")
    @Test
    void findAll() {
        Board board = squareDao.createBoard(1L);

        assertThat(board.getBoard().size()).isEqualTo(1);
    }
}
