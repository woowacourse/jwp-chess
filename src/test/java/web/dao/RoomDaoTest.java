package web.dao;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

import chess.Score;
import chess.piece.Color;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import web.dto.GameStatus;
import web.dto.RoomDto;

@JdbcTest
class RoomDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RoomDao roomDao;
    private ChessGameDao chessGameDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS room");
        jdbcTemplate.execute("DROP TABLE IF EXISTS piece");
        jdbcTemplate.execute("DROP TABLE IF EXISTS chess_game");
        jdbcTemplate.execute("CREATE TABLE chess_game\n"
                + "(\n"
                + "    id            INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,\n"
                + "    status        VARCHAR(10) NOT NULL,\n"
                + "    current_color CHAR(5)     NOT NULL,\n"
                + "    black_score   VARCHAR(10) NOT NULL,\n"
                + "    white_score   VARCHAR(10) NOT NULL\n"
                + ")");
        jdbcTemplate.execute("CREATE TABLE room\n"
                + "(\n"
                + "    id            INT         NOT NULL AUTO_INCREMENT PRIMARY KEY,\n"
                + "    chess_game_id INT         NOT NULL,\n"
                + "    name          VARCHAR(10) NOT NULL,\n"
                + "    password      VARCHAR(20) NOT NULL,\n"
                + "    FOREIGN KEY (chess_game_id) REFERENCES chess_game (id)\n"
                + ");");

        roomDao = new RoomDao(jdbcTemplate);
        chessGameDao = new ChessGameDao(jdbcTemplate);
    }

    @Test
    void findAll() {
        int chessGameId1 = chessGameDao.saveChessGame(
                GameStatus.READY, Color.WHITE, new Score(BigDecimal.ZERO), new Score(BigDecimal.ZERO));
        RoomDto roomDto1 = roomDao.saveRoom("verus", "1234", chessGameId1);

        int chessGameId2 = chessGameDao.saveChessGame(
                GameStatus.READY, Color.WHITE, new Score(BigDecimal.ZERO), new Score(BigDecimal.ZERO));
        RoomDto roomDto2 = roomDao.saveRoom("hoho", "1234", chessGameId2);

        assertThat(roomDao.findAll()).contains(roomDto1, roomDto2);
    }


    @Test
    void findById() {
        int chessGameId = chessGameDao.saveChessGame(
                GameStatus.READY, Color.WHITE, new Score(BigDecimal.ZERO), new Score(BigDecimal.ZERO));
        RoomDto roomDto = roomDao.saveRoom("verus", "1234", chessGameId);

        RoomDto expected = roomDao.findById(roomDto.getId());

        assertThat(expected.getName()).isEqualTo("verus");
        assertThat(expected.getPassword()).isEqualTo("1234");
    }

    @Test
    void findByName() {
        int chessGameId = chessGameDao.saveChessGame(
                GameStatus.READY, Color.WHITE, new Score(BigDecimal.ZERO), new Score(BigDecimal.ZERO));
        roomDao.saveRoom("verus", "1234", chessGameId);

        RoomDto expected = roomDao.findByName("verus");

        assertThat(expected.getName()).isEqualTo("verus");
        assertThat(expected.getPassword()).isEqualTo("1234");
    }

    @Test
    void deleteById() {
        int chessGameId = chessGameDao.saveChessGame(
                GameStatus.READY, Color.WHITE, new Score(BigDecimal.ZERO), new Score(BigDecimal.ZERO));
        RoomDto roomDto = roomDao.saveRoom("verus", "1234", chessGameId);

        roomDao.deleteById(roomDto.getId());

        assertThat(roomDao.findById(roomDto.getId())).isNull();
    }
}