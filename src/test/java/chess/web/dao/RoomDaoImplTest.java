package chess.web.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import chess.domain.game.ChessGame;
import chess.domain.game.state.Player;
import chess.domain.piece.Piece;
import chess.domain.piece.position.Position;
import chess.domain.piece.property.Color;
import chess.web.dto.CreateRoomRequestDto;
import chess.web.dto.DeleteDto;
import chess.web.dto.DeleteResultDto;
import chess.web.dto.FinishResultDto;
import chess.web.dto.ReadRoomResultDto;
import chess.web.dto.RoomDto;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
class RoomDaoImplTest {

    private RoomDao roomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomDao = new RoomDaoImpl(jdbcTemplate);

        jdbcTemplate.execute("create table room "
                + "("
                + "    id int(10) NOT NULL AUTO_INCREMENT, "
                + "    title varchar(255), "
                + "    password varchar(255) NOT NULL, "
                + "    color varchar(5) NOT NULL DEFAULT 'WHITE', "
                + "    finished boolean default 0, "
                + "    deleted boolean default 0,"
                + "    primary key (id) "
                + ");");

        jdbcTemplate.execute("insert into room (title, password) values ('testTitle', 'testPassword')");
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.execute("DROP TABLE room IF EXISTS;");
    }

    @Test
    void saveTurn() {
        roomDao.saveTurn(Color.WHITE, 1);

        Player player = roomDao.getPlayer(1);
        assertThat(player).isEqualTo(Player.of(Color.WHITE));
    }

    @Test
    void createRoom() {
        CreateRoomRequestDto createRoomRequestDto = new CreateRoomRequestDto("testTitle", "testPassword");
        int createdRoomId = roomDao.createRoom(createRoomRequestDto);
        Assertions.assertThat(createdRoomId).isNotNull();
    }

    @Test
    void findAll() {
        ReadRoomResultDto readRoomResultDto = roomDao.findAll();
        Assertions.assertThat(readRoomResultDto.getRooms().size()).isEqualTo(1);
    }

    @Test
    void changeTurn() {
        roomDao.changeTurn(1);

        Player player = roomDao.getPlayer(1);

        assertThat(player.getColor()).isEqualTo(Color.BLACK);
    }

    @Test
    void finish() {
        roomDao.finish(1);
        RoomDto roomDto = roomDao.isStartable(1);
        assertThat(roomDto.isFinished()).isTrue();
    }

    @Test
    void isStartable() {
        RoomDto roomDto = roomDao.isStartable(1);
        assertThat(roomDto.isFinished()).isEqualTo(false);
    }

    @Test
    void delete() {
        DeleteDto deleteDto = new DeleteDto("testPassword");
        DeleteResultDto deleteResultDto = roomDao.delete(1, deleteDto);
        assertThat(deleteResultDto.isDeleted()).isTrue();
    }

    @Test
    void getPlayer() {
        Player player = roomDao.getPlayer(1);
        assertThat(player.getColor()).isEqualTo(Color.WHITE);
    }
}
