package chess.dao;

import chess.dto.RoomDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource("classpath:application-test.properties")
class SpringChessRoomDaoTest {
    @Autowired
    private SpringChessRoomDao chessRoomDao;

    @BeforeEach
    void setUp() {
        chessRoomDao.createRoom("test1");
    }

    @AfterEach
    void tearDown() {
        chessRoomDao.findAllRoomIds().stream()
                .map(RoomDto::getRoomId)
                .forEach(id -> chessRoomDao.deleteRoom(id));
    }

    @Test
    @DisplayName("방 생성 기능")
    void createRoom() {
        chessRoomDao.createRoom("test2");
        List<String> roomNames = roomNames();

        assertThat(roomNames).hasSize(2);
        assertThat(roomNames).contains("test2");
    }

    @Test
    @DisplayName("방 목록 조회 기능")
    void findAll() {
        List<String> roomNames = roomNames();

        assertThat(roomNames).hasSize(1);
        assertThat(roomNames).containsExactly("test1");
    }

    @Test
    @DisplayName("방 삭제 기능")
    void deleteRoom() {
        String roomId = chessRoomDao.findAllRoomIds().stream()
                .map(RoomDto::getRoomId)
                .findFirst()
                .orElse("");
        chessRoomDao.deleteRoom(roomId);

        assertThat(roomNames()).hasSize(0);
    }

    private List<String> roomNames() {
        return chessRoomDao.findAllRoomIds().stream()
                .map(RoomDto::getRoomName)
                .collect(Collectors.toList());
    }
}