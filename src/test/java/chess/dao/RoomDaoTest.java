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
import java.util.Map;

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
        this.roomDao = new RoomDao(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @DisplayName("room 생성 테스트")
    @Test
    void insertRoomTest() {
        int fourthRoomId = roomDao.insertRoom("room4");
        assertEquals(4, fourthRoomId);
    }

    @DisplayName("room_id로 누구 차례인지 테스트")
    @Test
    void selectTurnByRoomIdTest() {
        roomDao.insertRoom("room1");
        String currentTurn = roomDao.selectTurnByRoomId(1);
        assertEquals("white", currentTurn);
    }

    @DisplayName("room_id로 순서 변경 테스트")
    @Test
    void changeTurnTest() {
        roomDao.insertRoom("room1");
        roomDao.changeTurn("black", "white", 1);
        String changedTurn = roomDao.selectTurnByRoomId(1);
        assertEquals("black", changedTurn);
    }

    @DisplayName("모든 방 이름 목록 테스트")
    @Test
    void selectAllRoomNamesTest() {
        List<Map<String, Object>> roomNames = roomDao.selectAllRoomNames();
        assertEquals(3, roomNames.size());
    }

    @DisplayName("방 이름으로 id 확인 테스트")
    @Test
    void selectRoomIdTest() {
        int firstRoomId = roomDao.selectRoomId("room1");
        int secondRoomId = roomDao.selectRoomId("room2");
        assertEquals(1, firstRoomId);
        assertEquals(2, secondRoomId);
    }
}
