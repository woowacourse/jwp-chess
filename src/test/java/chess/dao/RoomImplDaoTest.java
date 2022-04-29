package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import chess.entity.Room;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@TestPropertySource(properties = "spring.config.location=classpath:application-test.yml" )
@SpringBootTest
@Transactional
class RoomImplDaoTest {

    @Autowired
    private RoomImplDao roomDao;

    @Test
    @DisplayName("한개의 방을 생성할 수 있다.")
    void insertRoom() {
        Long roomId = roomDao.insertRoom("title1", "1111");

        assertThat(roomId).isInstanceOf(Long.class);
    }

    @Test
    @DisplayName("일련번호를 통해 방의 상태를 변경할 수 있다.")
    void updateStateById() {
        final Long roomId = insertTestRoom("title", "Ready");

        final Long updateRoomId = roomDao.updateStateById(roomId, "WhiteRunning");

        assertThat(roomDao.findRoomById(updateRoomId).getState()).isEqualTo("WhiteRunning");
    }

    @Test
    @DisplayName("일련번호를 통해 방을 조회할 수 있다.")
    void findRoomById() {
        final Long roomId = insertTestRoom("title", "1111");

        final Room room = roomDao.findRoomById(roomId);

        assertThat(room).isInstanceOf(Room.class);
    }

    @Test
    @DisplayName("일련번호를 통해 방을 삭제할 수 있다.")
    void deleteRoom() {
        final Long roomId = insertTestRoom("title", "1111");

        assertDoesNotThrow(() -> roomDao.deleteRoom(roomId));
    }

    private Long insertTestRoom(String title, String password) {
        return roomDao.insertRoom(title, password);
    }
}
