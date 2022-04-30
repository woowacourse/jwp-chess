package web.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import web.controller.Movement;
import web.dao.RoomDao;
import web.dto.RoomDto;
import web.exception.InvalidRoomRequestException;
import web.exception.IsRunningChessGameException;

@SpringBootTest
class RoomServiceTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ChessGameService chessGameService;

    @Autowired
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
    }

    @Test
    void saveRoom() {
        roomService.saveRoom("verus", "1234");
        assertThat(roomDao.findByName("verus")).isNotNull();
    }

    @Test
    void deleteRoom() {
        RoomDto roomDto = roomService.saveRoom("verus", "1234");

        roomService.deleteRoom(roomDto.getId(), "1234");

        assertThat(roomDao.findByName("verus")).isNull();
    }

    @Test
    void deleteRoomWithInvalidPassword() {
        RoomDto roomDto = roomService.saveRoom("verus", "1234");

        assertThatThrownBy(() -> roomService.deleteRoom(roomDto.getId(), "invalid"))
            .isInstanceOf(InvalidRoomRequestException.class);
    }

    @Test
    void deleteNotExistRoom() {
        assertThatThrownBy(() -> roomService.deleteRoom(23, "invalid"))
            .isInstanceOf(InvalidRoomRequestException.class);
    }

    @Test
    void deleteIsRunningRoom() {
        RoomDto roomDto = roomService.saveRoom("veurs", "1234");
        chessGameService.prepareNewChessGame(roomDto.getChessGameId());
        chessGameService.move(roomDto.getChessGameId(), new Movement("A2", "A4"));

        assertThatThrownBy(() -> roomService.deleteRoom(roomDto.getId(), "1234"))
            .isInstanceOf(IsRunningChessGameException.class);
    }


}