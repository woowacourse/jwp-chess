package chess.dao;

import static chess.model.Team.BLACK;
import static org.assertj.core.api.Assertions.assertThat;

import chess.model.Team;
import chess.model.board.Board;
import chess.model.piece.King;
import chess.model.piece.Pawn;
import chess.model.piece.Piece;
import chess.model.position.Position;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SquareDaoImplTest {

    @Autowired
    private SquareDaoImpl squareDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        squareDao = new SquareDaoImpl(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE IF EXISTS square");
        jdbcTemplate.execute("CREATE TABLE square(" +
                "position VARCHAR(2) NOT NULL, "
                + "team VARCHAR(10) NOT NULL, "
                + "symbol VARCHAR(10) NOT NULL, "
                + "PRIMARY KEY (position)"
                + ");");

        Position position = Position.from("a1");
        Piece piece = new Pawn(BLACK);

        jdbcTemplate.update("insert into square (position, team, symbol) values (?, ?, ?)",
                position.getKey(), piece.getTeam(), piece.getSymbol());
    }

    @DisplayName("데이터를 삽입한다.")
    @Test
    void insert() {
        Position position = Position.from("a2");
        Piece piece = new Pawn(Team.WHITE);

        squareDao.insert(position, piece);
        Board board = squareDao.createBoard();

        assertThat(board.getBoard().size()).isEqualTo(2);
    }

    @DisplayName("데이터를 삭제한다.")
    @Test
    void delete() {
        assertThat(squareDao.delete()).isEqualTo(1);
    }

    @DisplayName("데이터를 업데이트한다.")
    @Test
    void update() {
        Position position = Position.from("a1");
        Piece updatedPiece = new King(Team.WHITE);

        assertThat(squareDao.update(position, updatedPiece)).isEqualTo(1);
    }

    @DisplayName("데이터를 모두 가져온다.")
    @Test
    void findAll() {
        Board board = squareDao.createBoard();

        assertThat(board.getBoard().size()).isEqualTo(1);
    }
}
