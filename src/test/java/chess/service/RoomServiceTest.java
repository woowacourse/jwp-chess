package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.RoomDao;
import chess.dao.fake.FakeRoomDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class RoomServiceTest {

    private RoomDao roomDao;

    @BeforeEach
    void setUp() {
        roomDao = new FakeRoomDao();
    }

    @Test
    void search() {
        RoomService roomService = new RoomService(roomDao);
        assertThat(roomService.search()).isNull();
    }
}
