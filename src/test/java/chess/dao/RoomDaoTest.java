package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dto.RoomDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

@JdbcTest
public class RoomDaoTest {

    private RoomDao roomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomDao = new JdbcRoomDao(jdbcTemplate);

        jdbcTemplate.execute("drop table room if exists");
        jdbcTemplate.execute("CREATE TABLE room (\n"
                + "    id int not null auto_increment primary key,\n"
                + "    title varchar(30) not null,\n"
                + "    password varchar(8) not null\n)");
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
    void misMatchPassword() {
        RoomDto roomDto = new RoomDto("title", "password");
        roomDao.createRoom(roomDto);
        assertThat(roomDao.matchPassword(1, "passward")).isFalse();
    }

    @Test
    void misMatchId() {
        RoomDto roomDto = new RoomDto("title", "password");
        roomDao.createRoom(roomDto);
        assertThat(roomDao.matchPassword(2, "password")).isTrue();
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

        RoomDto newRoomDto = new RoomDto(roomDto.getTitle(), roomDto.getPassword(), roomDao.getRecentRoomId());
        roomDao.deleteRoom(newRoomDto);
        assertThat(roomDao.getRooms()).hasSize(0);
    }
}
