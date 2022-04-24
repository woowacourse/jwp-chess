package chess.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import chess.domain.piece.ChessmenInitializer;
import chess.domain.piece.Color;
import chess.domain.piece.King;
import chess.domain.piece.Piece;
import chess.domain.position.Position;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class PieceJdbcDaoTest {

    private PieceJdbcDao pieceJdbcDao;

    private GameJdbcDao gameJdbcDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameJdbcDao = new GameJdbcDao(jdbcTemplate);
        pieceJdbcDao = new PieceJdbcDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE game IF EXISTS");
        jdbcTemplate.execute("create table game("
            + "id varchar(100) not null unique, "
            + "turn varchar(10) not null, "
            + "force_end_flag tinyint(1) not null default false"
            + ")");

        jdbcTemplate.execute("DROP TABLE piece IF EXISTS");
        jdbcTemplate.execute("create table piece("
            + "id int AUTO_INCREMENT PRIMARY KEY, "
            + "name varchar(10) not null, "
            + "color varchar(10) not null, "
            + "position varchar(10) not null, "
            + "game_id varchar(100) not null, "
            + "foreign key (game_id) references game (id)"
            + ")");
    }

    @Test
    void createAllById() {
        gameJdbcDao.createById("1234");

        final ChessmenInitializer chessmenInitializer = new ChessmenInitializer();
        final List<Piece> pieces = chessmenInitializer.init().getPieces();
        pieceJdbcDao.createAllById(pieces, "1234");

        assertThat(pieceJdbcDao.findAllByGameId("1234").getPieces().size()).isEqualTo(32);
    }

    @Test
    void updateAllByGameId() {
        gameJdbcDao.createById("1234");
        final ChessmenInitializer chessmenInitializer = new ChessmenInitializer();
        final List<Piece> pieces = chessmenInitializer.init().getPieces();
        pieceJdbcDao.createAllById(pieces, "1234");

        final List<Piece> move = List.of(new King(Color.BLACK, Position.of("h2")));

        pieceJdbcDao.updateAllByGameId(move, "1234");


        assertThat(pieceJdbcDao.findAllByGameId("1234").getPieces().size()).isEqualTo(1);
    }

    @Test
    void deleteAllByGameId() {
        gameJdbcDao.createById("1234");

        final ChessmenInitializer chessmenInitializer = new ChessmenInitializer();
        final List<Piece> pieces = chessmenInitializer.init().getPieces();
        pieceJdbcDao.createAllById(pieces, "1234");
        pieceJdbcDao.deleteAllByGameId("1234");

        assertThat(pieceJdbcDao.findAllByGameId("1234").getPieces().size()).isEqualTo(0);
    }

//    @Test
//    void findAllbyGameId() {
//        gameJdbcDao = new GameJdbcDao(jdbcTemplate);
//        int count = gameJdbcDao.count();
//
//        assertThat(count).isEqualTo(0);
//    }

}
