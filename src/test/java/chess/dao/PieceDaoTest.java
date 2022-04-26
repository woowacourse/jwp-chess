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
    private List<Piece> pieces;
    private long gameId;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao(jdbcTemplate);
        pieceDao = new PieceDao(jdbcTemplate);

        jdbcTemplate.execute("DROP TABLE piece IF EXISTS");
        jdbcTemplate.execute("DROP TABLE game IF EXISTS");

        jdbcTemplate.execute("create table game("
            + "id int AUTO_INCREMENT PRIMARY KEY, "
            + "turn varchar(10) not null default 'black',"
            + "force_end_flag tinyint(1) not null default false,"
            + "title varchar(100) not null,"
            + "password varchar(100) not null"
            + ")");

        jdbcTemplate.execute("create table piece("
            + "id int AUTO_INCREMENT PRIMARY KEY, "
            + "name varchar(10) not null, "
            + "color varchar(10) not null, "
            + "position varchar(10) not null, "
            + "game_id int not null, "
            + "foreign key (game_id) references game (id)"
            + ")");

        gameId = gameDao.createByTitleAndPassword("게임방제목", "password486");
        pieces = ChessmenInitializer.init().getPieces();
    }

    @AfterAll
    void end() {
        jdbcTemplate.execute("DROP TABLE piece IF EXISTS");
        jdbcTemplate.execute("DROP TABLE game IF EXISTS");
    }

    @DisplayName("createAllByGameId 실행시 해당 gameId에 piece 정보들이 추가된다.")
    @Test
    void createAllById() {
        pieceDao.createAllByGameId(pieces, gameId);

        assertThat(pieceDao.findAllByGameId(gameId).getPieces().size()).isEqualTo(32);
    }

    @DisplayName("updateAllByGameId 실행시 해당 gameId의 piece들의 정보가 바뀐다.")
    @Test
    void updateAllByGameId() {
        pieceDao.createAllByGameId(pieces, gameId);
        pieces.remove(pieces.size() - 1);
        pieces.add(new King(Color.BLACK, Position.of("h2")));

        pieceDao.updateAllByGameId(pieces, gameId);
        Pieces moved = pieceDao.findAllByGameId(gameId);

        assertThat(moved.extractPiece(Position.of("h2")).getName()).isEqualTo("king");
    }

    @DisplayName("updateAllByGameId 실행시 해당 gameId의 piece들이 사라진다.")
    @Test
    void deleteAllByGameId() {
        pieceDao.createAllByGameId(pieces, gameId);
        pieceDao.deleteAllByGameId(gameId);

        assertThat(pieceDao.findAllByGameId(gameId).getPieces().size()).isEqualTo(0);
    }

}
