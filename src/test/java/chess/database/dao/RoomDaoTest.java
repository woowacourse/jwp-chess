package chess.database.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import chess.database.dto.RoomDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

@Sql("/sql/chess-test.sql")
@SpringBootTest
public class RoomDaoTest {

    private static final String TEST_ROOM_NAME = "TESTING";
    private static final String TEST_ROOM_PASSWORD = "1234";
    private static final String TEST_CREATION_ROOM_NAME = "TESTING2";
    private static final String TEST_CREATION_ROOM_PASSWORD = "4321";

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private RoomDao roomDao;

    @BeforeEach
    void setUp() {
        roomDao = new RoomDao(jdbcTemplate);
        roomDao.create(new RoomDto(TEST_ROOM_NAME, TEST_ROOM_PASSWORD));
    }


    @Test
    @DisplayName("게임방을 생성한다.")
    public void createRoom() {
        RoomDto roomDto = new RoomDto(TEST_CREATION_ROOM_NAME, TEST_CREATION_ROOM_PASSWORD);
        assertThatCode(() -> roomDao.create(roomDto))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("게임방 이름으로 검색한다.")
    public void findByRoomName() {
        assertThat(roomDao.findByName(TEST_ROOM_NAME).getPassword()).isEqualTo(TEST_ROOM_PASSWORD);
    }

    @Test
    @DisplayName("게임방 이름과 비밀번호로 삭제한다.")
    public void delete() {
        RoomDto roomDto = new RoomDto(TEST_ROOM_NAME, TEST_ROOM_PASSWORD);
        int id = roomDao.findByName(TEST_ROOM_NAME).getId();
        assertThatCode(() -> roomDao.delete(id))
            .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("게임방 목록을 검색한다.")
    public void findAll() {
        assertThat(roomDao.findAll().size()).isEqualTo(1);
    }
}
