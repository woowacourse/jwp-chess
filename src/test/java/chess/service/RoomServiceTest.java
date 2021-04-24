package chess.service;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.FakeChessDao;
import chess.dao.FakeRoomDao;
import chess.domain.room.Room;

public class RoomServiceTest {

    private final RoomService roomService;

    public RoomServiceTest() {
        this.roomService = new RoomService(new FakeRoomDao(), new FakeChessDao());
    }

    @DisplayName("모든 방 목록 조회 테스트")
    @Test
    void findAllTest() {

        // given
        Room room = new Room(1L, "테스트");
        roomService.insert(room);

        // when
        final List<Room> rooms = roomService.findAll();

        // then
        assertThat(rooms).containsExactly(room);
    }

    @DisplayName("룸 ID로 체스 조회 테스트")
    @Test
    void findChessIdByIdTest() {

        // given
        Room room = new Room(1L, "테스트");
        final long roomId = roomService.insert(room);

        // when
        final long chessId = roomService.findChessIdById(roomId);

        // then
        assertThat(chessId).isEqualTo(1L);
    }

    @DisplayName("방 생성 테스트")
    @Test
    void insertTest() {

        // given
        Room room = new Room(1L, "테스트");

        // when
        roomService.insert(room);

        // then
        final List<Room> rooms = roomService.findAll();
        assertThat(rooms).containsExactly(room);
    }
}
