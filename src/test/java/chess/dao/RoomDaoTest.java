package chess.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
@Sql("classpath:schema.sql")
public class RoomDaoTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private RoomDao roomDao;

    @BeforeEach
    void setUp() {
        roomDao = new RoomDao(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @DisplayName("room 생성 테스트")
    @Test
    void insertRoomTest() {
        int firstRoomId = roomDao.insertRoom("방1");
        assertEquals(1, firstRoomId);

        int secondRoomId = roomDao.insertRoom("방2");
        assertEquals(2, secondRoomId);
    }

    @DisplayName("room_id로 누구 차례인지 테스트")
    @Test
    void selectTurnByRoomIdTest() {
        roomDao.insertRoom("방1");
        String currentTurn = roomDao.selectTurnByRoomId(1);
        assertEquals("white", currentTurn);
    }

    @DisplayName("room_id로 순서 변경 테스트")
    @Test
    void changeTurnTest() {
        roomDao.insertRoom("방1");
        roomDao.changeTurn("black", "white", 1);
        String changedTurn = roomDao.selectTurnByRoomId(1);
        assertEquals("black", changedTurn);
    }

    @DisplayName("모든 방 이름 목록 테스트")
    @Test
    void selectAllRoomNamesTest() {
        roomDao.insertRoom("방1");
        roomDao.insertRoom("방2");
        roomDao.insertRoom("방3");
        List<String> roomNames = roomDao.selectAllRoomNames();
        assertEquals(3, roomNames.size());
    }

    @DisplayName("방 이름으로 id 확인 테스트")
    @Test
    void selectRoomIdTest() {
        roomDao.insertRoom("방1");
        roomDao.insertRoom("방2");
        int firstRoomId = roomDao.selectRoomId("방1");
        int secondRoomId = roomDao.selectRoomId("방2");
        assertEquals(1, firstRoomId);
        assertEquals(2, secondRoomId);
    }
}
