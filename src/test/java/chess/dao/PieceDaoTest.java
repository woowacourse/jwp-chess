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

        jdbcTemplate.execute("drop table game if exists");
        jdbcTemplate.execute("create table game("
                + "game_id int primary key not null,"
                + " current_turn varchar(10) default'WHITE')");
        jdbcTemplate.update("insert into game(game_id, current_turn) values (?,?)",
                0, "WHITE");

        jdbcTemplate.execute("drop table piece if exists");
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
        List<BoardElementDto> pieces = pieceDao.findAllPiece(gameId);
        assertThat(pieces.contains(boardElementDto)).isTrue();
    }
}
