package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.RoomDao;
import chess.dao.fake.FakeRoomDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoomServiceTest {

    private RoomDao roomDao;

    @BeforeEach
    void setUp() {
        roomDao = new FakeRoomDao();
    }

    @DisplayName("체스방의 정보를 가져온다.")
    @Test
    void search() {
        RoomService roomService = new RoomService(roomDao);
        assertThat(roomService.searchRooms()).isEmpty();
    }

    @DisplayName("세로운 체스방을 생성하고 모든 체스방의 정보를 가져온다.")
    @Test
    void create() {
        RoomService roomService = new RoomService(roomDao);
        assertThat(roomService.createRoom("알파룸", "1234")).isNotEmpty();
    }
}
