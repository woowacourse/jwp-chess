package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dto.RoomDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@JdbcTest
@Sql("classpath:roomInit.sql")
public class RoomDaoTest {

    private RoomDao roomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomDao = new JdbcRoomDao(jdbcTemplate);
    }

    @Test
    void createRoom() {
        RoomDto room = new RoomDto("title", "password");
        RoomDto room1 = new RoomDto("title2", "password");
        roomDao.createRoom(room);
        roomDao.createRoom(room1);
        assertThat(roomDao.getRecentRoomId()).isEqualTo(2);
    }

    @Test
    void matchPassword() {
        RoomDto roomDto = new RoomDto("title", "password");
        roomDao.createRoom(roomDto);
        assertThat(roomDao.matchPassword(1, "password")).isTrue();
    }

    @Test
    @DisplayName("password가 일치하지 않을 때")
    void misMatchPassword() {
        RoomDto roomDto = new RoomDto("title", "password");
        roomDao.createRoom(roomDto);
        assertThat(roomDao.matchPassword(1, "passward")).isFalse();
    }

    @Test
    @DisplayName("id가 일치하지 않을 때")
    void misMatchId() {
        RoomDto roomDto = new RoomDto("title", "password");
        roomDao.createRoom(roomDto);
        assertThat(roomDao.matchPassword(2, "password")).isFalse();
    }

    @Test
    void getRooms() {
        RoomDto roomDto1 = new RoomDto("1", "1234");
        RoomDto roomDto2 = new RoomDto("2", "2341");
        RoomDto roomDto3 = new RoomDto("3", "3451");

        roomDao.createRoom(roomDto1);
        roomDao.createRoom(roomDto2);
        roomDao.createRoom(roomDto3);

        assertThat(roomDao.getRooms()).hasSize(3);
    }

    @Test
    void deleteRoom() {
        RoomDto roomDto = new RoomDto("1", "1234");
        roomDao.createRoom(roomDto);
        assertThat(roomDao.getRooms()).hasSize(1);

        RoomDto newRoomDto = new RoomDto( roomDao.getRecentRoomId(), roomDto.getTitle(), roomDto.getPassword());
        roomDao.deleteRoom(newRoomDto);
        assertThat(roomDao.getRooms()).hasSize(0);
    }
}
