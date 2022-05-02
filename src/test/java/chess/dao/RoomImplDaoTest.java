package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import chess.entity.Room;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@TestPropertySource(properties = "spring.config.location=classpath:application-test.yml")
@SpringBootTest
@Transactional
class RoomImplDaoTest {

    @Autowired
    private RoomImplDao roomDao;

    @Test
    @DisplayName("전체 방 리스트를 조회할 수 있다.")
    void findAllRoom() {
        insertTestRoom("title1", "1111");
        insertTestRoom("title2", "1111");

        final List<Room> rooms = roomDao.findAllRoom();

        assertThat(rooms).hasSize(2)
                .extracting("state", "title", "password")
                .contains(
                        tuple("Ready", "title1", "1111"),
                        tuple("Ready", "title1", "1111")
                );
    }

    @Test
    @DisplayName("한개의 방을 생성할 수 있다.")
    void insertRoom() {
        Long roomId = roomDao.insertRoom("title1", "1111");

        assertThat(roomDao.findRoomById(roomId))
                .extracting("state", "title", "password")
                .contains("Ready", "title1", "1111");
    }

    @Test
    @DisplayName("일련번호를 통해 방의 상태를 변경할 수 있다.")
    void updateStateById() {
        final Long roomId = insertTestRoom("title1", "1111");

        final Long updateRoomId = roomDao.updateStateById(roomId, "WhiteRunning");

        assertThat(roomDao.findRoomById(updateRoomId))
                .extracting("state", "title", "password")
                .contains("WhiteRunning", "title1", "1111");
    }

    @Test
    @DisplayName("일련번호를 통해 방을 조회할 수 있다.")
    void findRoomById() {
        final Long roomId = insertTestRoom("title1", "1111");

        assertThat(roomDao.findRoomById(roomId))
                .extracting("state", "title", "password")
                .contains("Ready", "title1", "1111");
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
