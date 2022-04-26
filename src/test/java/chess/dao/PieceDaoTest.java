package chess.dao;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import chess.domain.piece.ChessmenInitializer;
import chess.domain.piece.Color;
import chess.domain.piece.King;
import chess.domain.piece.Piece;
import chess.domain.piece.Pieces;
import chess.domain.position.Position;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@TestInstance(Lifecycle.PER_CLASS)
@JdbcTest
public class PieceDaoTest {

    private PieceDao pieceDao;
    private GameDao gameDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao(jdbcTemplate);
        pieceDao = new PieceDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE piece IF EXISTS");
        jdbcTemplate.execute("DROP TABLE game IF EXISTS");

        jdbcTemplate.execute("create table game("
            + "id varchar(100) not null unique, "
            + "turn varchar(10) not null, "
            + "force_end_flag tinyint(1) not null default false"
            + ")");

        jdbcTemplate.execute("create table piece("
            + "id int AUTO_INCREMENT PRIMARY KEY, "
            + "name varchar(10) not null, "
            + "color varchar(10) not null, "
            + "position varchar(10) not null, "
            + "game_id varchar(100) not null, "
            + "foreign key (game_id) references game (id)"
            + ")");
    }

    @AfterAll
    void end() {
        jdbcTemplate.execute("DROP TABLE piece IF EXISTS");
        jdbcTemplate.execute("DROP TABLE game IF EXISTS");
    }

    @DisplayName("createAllByGameId 실행시 해당 gameId에 piece 정보들이 추가된다.")
    @Test
    void createAllById() {
        gameDao.createById("1234");

        final ChessmenInitializer chessmenInitializer = new ChessmenInitializer();
        final List<Piece> pieces = chessmenInitializer.init().getPieces();
        pieceDao.createAllById(pieces, "1234");

        assertThat(pieceDao.findAllByGameId("1234").getPieces().size()).isEqualTo(32);
    }

    @DisplayName("updateAllByGameId 실행시 해당 gameId의 piece들의 정보가 바뀐다.")
    @Test
    void updateAllByGameId() {
        gameDao.createById("1234");
        final ChessmenInitializer chessmenInitializer = new ChessmenInitializer();
        final List<Piece> pieces = chessmenInitializer.init().getPieces();
        pieceDao.createAllById(pieces, "1234");

        pieces.remove(pieces.size() - 1);
        pieces.add(new King(Color.BLACK, Position.of("h2")));

        pieceDao.updateAllByGameId(pieces, "1234");
        Pieces move = pieceDao.findAllByGameId("1234");

        assertThat(move.extractPiece(Position.of("h2")).getName()).isEqualTo("king");
    }

    @DisplayName("updateAllByGameId 실행시 해당 gameId의 piece들이 사라진다.")
    @Test
    void deleteAllByGameId() {
        gameDao.createById("1234");

        final ChessmenInitializer chessmenInitializer = new ChessmenInitializer();
        final List<Piece> pieces = chessmenInitializer.init().getPieces();
        pieceDao.createAllById(pieces, "1234");
        pieceDao.deleteAllByGameId("1234");

        assertThat(pieceDao.findAllByGameId("1234").getPieces().size()).isEqualTo(0);
    }

}
