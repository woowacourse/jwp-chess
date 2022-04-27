package chess.dao;

import chess.dto.RoomDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class RoomDaoTest {

    private RoomDao roomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomDao = new JdbcRoomDao(jdbcTemplate);

        jdbcTemplate.execute("drop table room if exists");
        jdbcTemplate.execute("CREATE TABLE room (\n" +
                "    id bigint not null auto_increment primary key,\n" +
                "    name varchar(30) not null,\n" +
                "    password varchar(20) not null)"
        );
    }

    @Test
    @DisplayName("체스룸을 생성한다.")
    void makeRoom() {
        final RoomDto roomDto = new RoomDto("chessRoom", "abcd");
        final long expected = 1L;

        final long actual = roomDao.makeRoom(roomDto);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("체스룸 제목으로 방 id를 찾는다.")
    void findIdByRoomName() {
        final String roomName = "chessRoom";
        final RoomDto roomDto = new RoomDto(roomName, "abcd");
        roomDao.makeRoom(roomDto);
        final long expected = 1L;

        final long actual = roomDao.findIdByRoomName(roomName);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("체스룸 id로 방을 찾는다.")
    void findRoomById() {
        final RoomDto roomDto1 = new RoomDto("chessRoom", "abcd");
        final RoomDto roomDto2 = new RoomDto("chessRoom2", "1234");
        roomDao.makeRoom(roomDto1);
        roomDao.makeRoom(roomDto2);

        final RoomDto actual = roomDao.findRoomById(2L);

        assertThat(actual).isEqualTo(roomDto2);
    }

    @Test
    @DisplayName("체스룸을 삭제한다.")
    void deleteRoom() {
        final RoomDto roomDto = new RoomDto("chessRoom", "abcd");
        roomDao.makeRoom(roomDto);
        roomDao.deleteRoom(1L);
        final String sql = "select count(*) from room";
        final int expected = 0;

        final int actual = jdbcTemplate.queryForObject(sql, Integer.class);

        assertThat(actual).isEqualTo(expected);
    }
}