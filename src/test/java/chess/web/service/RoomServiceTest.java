package chess.web.service;

import chess.domain.entity.Room;
import chess.web.dao.PieceDao;
import chess.web.dao.RoomDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class RoomServiceTest {

    private final RoomService roomService = new RoomService(
            new MockRoomDao(), new MockPieceDao()
    );

    private final Long ProceedingRoomId = 1L;
    private final Long finishedRoomId = 2L;

    @Test
    @DisplayName("체스판이 만들어진다.")
    public void createRoom() {
        Long id = roomService.createRoom("방 제목", "비밀번호");
        assertThat(id).isEqualTo(ProceedingRoomId);
    }

    @Test
    @DisplayName("체스 경기가 끝나지않았다면 방이 삭제되지 않는다.")
    public void deleteFailureWhenNotFinish() {

        assertThatThrownBy(
                () -> roomService.delete("비밀번호", ProceedingRoomId)
        ).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("2개의 방 중 한 방을 지우면 남은 방은 한 개가 된다.")
    public void delete() {
        roomService.delete("비밀번호", finishedRoomId);
        List<Room> roomList = roomService.getRoomList();
        assertThat(roomList.size()).isEqualTo(1);
    }
}
