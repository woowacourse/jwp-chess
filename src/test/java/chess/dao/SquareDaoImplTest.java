package chess.dao;

import static chess.model.Team.BLACK;
import static org.assertj.core.api.Assertions.assertThat;

import chess.model.Team;
import chess.model.board.Board;
import chess.model.piece.King;
import chess.model.piece.Pawn;
import chess.model.piece.Piece;
import chess.model.position.Position;
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
        jdbcTemplate.execute("CREATE TABLE square("
                + "id VARCHAR(2) NOT NULL, "
                + "position VARCHAR(2) NOT NULL, "
                + "team VARCHAR(10) NOT NULL, "
                + "symbol VARCHAR(10) NOT NULL, "
                + "PRIMARY KEY (id, position)"
                + ");");

        String id = "1";
        Position position = Position.from("a1");
        Piece piece = new Pawn(BLACK);

        jdbcTemplate.update("insert into square (id, position, team, symbol) values (?, ?, ?, ?)",
                id, position.getKey(), piece.getTeam(), piece.getSymbol());
    }

    @DisplayName("데이터를 삽입한다.")
    @Test
    void insert() {
        String id = "2";
        Position position = Position.from("a2");
        Piece piece = new Pawn(Team.WHITE);

        assertThat(squareDao.insert(id, position, piece)).isEqualTo(1);
    }

    @DisplayName("데이터를 삭제한다.")
    @Test
    void delete() {
        String id = "1";
        assertThat(squareDao.deleteFrom(id)).isEqualTo(1);
    }

    @DisplayName("데이터를 업데이트한다.")
    @Test
    void update() {
        String id = "1";
        Position position = Position.from("a1");
        Piece updatedPiece = new King(Team.WHITE);

        assertThat(squareDao.update(id, position, updatedPiece)).isEqualTo(1);
    }

    @DisplayName("데이터를 가져온다.")
    @Test
    void findAll() {
        String id = "1";
        Board board = squareDao.createBoardFrom(id);

        assertThat(board.getBoard().size()).isEqualTo(1);
    }
}
