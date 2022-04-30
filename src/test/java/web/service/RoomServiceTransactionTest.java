package web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import web.dao.ChessGameDao;
import web.dao.RoomDao;

@SpringBootTest
public class RoomServiceTransactionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ChessGameDao chessGameDao;

    @MockBean
    private RoomDao roomDao;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS piece");
        jdbcTemplate.execute("DROP TABLE IF EXISTS room");
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
        jdbcTemplate.execute("CREATE TABLE piece\n"
            + "(\n"
            + "    position      CHAR(2)     NOT NULL,\n"
            + "    chess_game_id INT         NOT NULL,\n"
            + "    color         CHAR(5)     NOT NULL,\n"
            + "    type          VARCHAR(10) NOT NULL,\n"
            + "    PRIMARY KEY (position, chess_game_id),\n"
            + "    FOREIGN KEY (chess_game_id) REFERENCES chess_game (id)\n"
            + ")");

        Mockito.when(roomDao.saveRoom(anyString(), anyString(), anyInt()))
            .thenThrow(RuntimeException.class);
    }

    @Test
    void applyTransaction() {
        try {
            roomService.saveRoom("verus", "1234");
            fail("saveRoom 메서드 예외 미발생");
        } catch (RuntimeException ignore) { }

        assertThat(chessGameDao.findAll()).hasSize(0);
    }
}
