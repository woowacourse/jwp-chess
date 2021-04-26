package chess.domain.dao;

import chess.controller.spring.params.Page;
import chess.dao.RoomDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RoomDAOTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RoomDAO roomDao;

    @BeforeEach
    public void setup() {
        jdbcTemplate.execute("TRUNCATE TABLE piece");
        jdbcTemplate.execute("TRUNCATE TABLE grid");
        jdbcTemplate.execute("TRUNCATE TABLE room");
    }

    @Test
    @DisplayName("방을 정상적으로 조회 및 추가를 하는 지 테스트")
    public void createAndFindRoom() {
        String roomName = "abcd";
        long roomId = roomDao.createRoom(roomName);
        Object foundRoomId = roomDao.findRoomIdByName(roomName);
        assertThat(Optional.ofNullable(roomId)).isEqualTo(foundRoomId);
    }

    @Test
    @DisplayName("방의 개수를 20개로 제한해서 불러오는 지 테스트")
    public void findAllRooms() {
        for (int i = 0; i < 50; i++) {
            roomDao.createRoom(i + "");
        }
        assertThat(roomDao.findAllRooms().size()).isEqualTo(20);
    }

    @Test
    @DisplayName("페이징 기능이 제대로 동작하는 지 테스트")
    public void findAllRooms_Offset() {
        for (int i = 1; i <= 20; i++) {
            roomDao.createRoom(i + "");
        }
        long roomId = roomDao.createRoom("ab");
        assertThat(roomDao.findAllRooms(new Page(2)).get(0).getRoomId()).isEqualTo(roomId);
    }
}