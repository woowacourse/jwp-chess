package chess.dao;

import static org.assertj.core.api.Assertions.*;

import chess.dto.BoardElementDto;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class PieceDaoTest {
    private GameDao gameDao;
    private PieceDao pieceDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        gameDao = new GameDao(jdbcTemplate);
        pieceDao = new PieceDao(jdbcTemplate);

        jdbcTemplate.execute("drop table piece if exists");
        jdbcTemplate.execute("drop table game if exists");

        jdbcTemplate.execute("create table game("
                + "game_id int primary key not null,"
                + " current_turn varchar(10) default'WHITE')");
        jdbcTemplate.update("insert into game(game_id, current_turn) values (?,?)",
                0, "WHITE");

        jdbcTemplate.execute("create table piece("
                + "piece_id int primary key auto_increment,"
                + "game_id int not null,"
                + "piece_name varchar(10) not null,"
                + "piece_color varchar(10) not null,"
                + "position varchar(2) not null,"
                + "foreign key (game_id) references game(game_id))");
    }

    @Test
    void savePieceTest() {
        int gameId = 0;
        BoardElementDto boardElementDto = new BoardElementDto("p", "WHITE", "a2");
        pieceDao.savePiece(gameId, boardElementDto);
        List<BoardElementDto> pieces = pieceDao.findAllPieceById(gameId);
        assertThat(pieces.contains(boardElementDto)).isTrue();
    }

    @Test
    void findAllPieceByIdTest() {
        int gameId = 0;
        BoardElementDto boardElementDto1 = new BoardElementDto("p", "WHITE", "a2");
        BoardElementDto boardElementDto2 = new BoardElementDto("k", "BLACK", "c7");
        pieceDao.savePiece(gameId, boardElementDto1);
        pieceDao.savePiece(gameId, boardElementDto2);
        List<BoardElementDto> pieces = pieceDao.findAllPieceById(gameId);
        assertThat(pieces.contains(boardElementDto1) && pieces.contains(boardElementDto2)).isTrue();
    }

    @Test
    void deleteAllPieceByIdTest() {
        int gameId = 0;
        pieceDao.deleteAllPieceById(gameId);
        List<BoardElementDto> pieces = pieceDao.findAllPieceById(gameId);
        assertThat(pieces.isEmpty()).isTrue();
    }

    @Test
    void deletePieceByIdAndPositionTest() {
        int gameId = 0;
        String position = "a2";
        BoardElementDto boardElementDto = new BoardElementDto("p", "WHITE", position);
        pieceDao.savePiece(gameId, boardElementDto);
        pieceDao.deletePieceByIdAndPosition(gameId, position);
        List<BoardElementDto> pieces = pieceDao.findAllPieceById(gameId);
        assertThat(pieces.isEmpty()).isTrue();
    }

    @Test
    void updatePiecePositionTest() {
        int gameId = 0;
        String from = "a2";;
        String to = "a3";
        BoardElementDto boardElementDto = new BoardElementDto("p", "WHITE", from);
        pieceDao.savePiece(gameId, boardElementDto);
        pieceDao.updatePiecePosition(gameId, from, to);
        BoardElementDto piece = pieceDao.findAllPieceById(gameId).get(0);
        assertThat(piece.getPosition()).isEqualTo(to);
    }
}
