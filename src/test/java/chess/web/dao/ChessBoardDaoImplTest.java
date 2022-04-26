package chess.web.dao;

import static org.assertj.core.api.Assertions.*;

import chess.domain.piece.Piece;
import chess.domain.piece.StartedPawn;
import chess.domain.piece.position.Position;
import chess.domain.piece.property.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class ChessBoardDaoImplTest {

    private ChessBoardDao chessBoardDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        chessBoardDao = new ChessBoardDaoImpl(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE board IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE board(" +
                "position VARCHAR(255) NOT NULL, piece VARCHAR(255) NOT NULL, PRIMARY KEY (position))");
    }

    @Test
    void save() {
        Position position = Position.of("a2");
        Piece piece = new StartedPawn(Color.WHITE);

        chessBoardDao.save(position, piece);

        Map<Position, Piece> board = chessBoardDao.findAll();
        assertThat(board).hasSize(1);
    }

    @Test
    void deleteAll() {
        chessBoardDao.deleteAll();

        Map<Position, Piece> board = chessBoardDao.findAll();
        assertThat(board).hasSize(0);
    }

    @Test
    void findAll() {
        Map<Position, Piece> board = chessBoardDao.findAll();

        assertThat(board).isNotNull();
    }
}
