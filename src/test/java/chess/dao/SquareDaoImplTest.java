package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.model.Team;
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
    private SquareDaoImpl2 squareDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        squareDao = new SquareDaoImpl2(jdbcTemplate);
        jdbcTemplate.execute("DROP TABLE IF EXISTS square");
        jdbcTemplate.execute("CREATE TABLE square(" +
                "position VARCHAR(2) NOT NULL, "
                + "team VARCHAR(10) NOT NULL, "
                + "symbol VARCHAR(10) NOT NULL, "
                + "PRIMARY KEY (position)"
                + ");");
    }

    @DisplayName("데이터를 삽입한다.")
    @Test
    void insert() {
        Position position = Position.from("a2");
        Piece piece = new Pawn(Team.WHITE);

        squareDao.insert(position, piece);
        Piece a = squareDao.find(position);

        assertThat(a.getSymbol()).isEqualTo("PAWN");
    }

    @DisplayName("데이터를 삭제한다.")
    @Test
    void delete() {
        Position position = Position.from("a2");
        Piece piece = new Pawn(Team.WHITE);

        squareDao.insert(position, piece);

        assertThat(squareDao.delete()).isEqualTo(1);
    }

    @DisplayName("데이터를 업데이트한다.")
    @Test
    void update() {
        Position position = Position.from("a2");
        Piece piece = new Pawn(Team.WHITE);
        squareDao.insert(position, piece);

        Piece updatedPiece = new King(Team.WHITE);
        squareDao.update(position, updatedPiece);

        assertThat(squareDao.find(position).getSymbol()).isEqualTo("KING");
    }
}
