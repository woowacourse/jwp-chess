package chess.dao;

import chess.dto.RoomDto;
import chess.dto.RoomRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class RoomDaoTest extends DaoTest {

    private RoomDao roomDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        roomDao = new JdbcRoomDao(jdbcTemplate);

        super.setUp();
    }

    @Test
    @DisplayName("체스룸을 생성한다.")
    void makeRoom() {
        final RoomRequestDto roomRequestDto = new RoomRequestDto("chessRoom", "abcd");
        final long expected = 1L;

        final long actual = roomDao.makeRoom(roomRequestDto);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("체스룸 제목으로 방 id를 찾는다.")
    void findIdByRoomName() {
        final String roomName = "chessRoom";
        final RoomRequestDto roomRequestDto = new RoomRequestDto(roomName, "abcd");
        roomDao.makeRoom(roomRequestDto);
        final long expected = 1L;

        final long actual = roomDao.findIdByRoomName(roomName);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("체스룸 id로 방을 찾는다.")
    void findRoomById() {
        final RoomRequestDto roomRequestDto1 = new RoomRequestDto("chessRoom", "abcd");
        final RoomRequestDto roomRequestDto2 = new RoomRequestDto("chessRoom2", "1234");
        roomDao.makeRoom(roomRequestDto1);
        roomDao.makeRoom(roomRequestDto2);

        final RoomDto actual = roomDao.findRoomById(2L);

        assertThat(actual.getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("체스룸을 삭제한다.")
    void deleteRoom() {
        final RoomRequestDto roomRequestDto = new RoomRequestDto("chessRoom", "abcd");
        roomDao.makeRoom(roomRequestDto);
        roomDao.deleteRoom(1L);
        final String sql = "select count(*) from room";
        final int expected = 0;

        final int actual = jdbcTemplate.queryForObject(sql, Integer.class);

        assertThat(actual).isEqualTo(expected);
    }
}