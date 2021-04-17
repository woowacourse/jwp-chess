package chess.domain.dao;

import chess.dao.RoomDAO;
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

    @Test
    @DisplayName("방을 정상적으로 조회 및 추가를 하는 지 테스트")
    public void createAndFindRoom() {
        RoomDAO roomDao = new RoomDAO(jdbcTemplate);
        String roomName = "abcd";
        long roomId = roomDao.createRoom(roomName);
        Object foundRoomId = roomDao.findRoomIdByName(roomName);
        assertThat(Optional.ofNullable(roomId)).isEqualTo(foundRoomId);
    }
}