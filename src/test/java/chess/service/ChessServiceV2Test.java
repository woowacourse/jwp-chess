package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.RoomFakeDao;
import chess.dao.SquareFakeDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChessServiceV2Test {

    private final ChessServiceV2 chessServiceV2 = new ChessServiceV2(new RoomFakeDao(), new SquareFakeDao());

    @Test
    @DisplayName("한개의 방을 생성할 수 있다.")
    void insertRoom() {
        final Long roomId = chessServiceV2.insertRoom("title1", "1111");

        assertThat(roomId).isInstanceOf(Long.class);
    }

    @Test
    @DisplayName("방의 일련번호를 통해 보드판과 방의 상태를 변경할 수 있다.")
    void insertBoard() {
        final Long roomId = chessServiceV2.insertRoom("title1", "1111");
        final Long updateRoomId = chessServiceV2.insertBoard(roomId);

        assertThat(updateRoomId).isInstanceOf(Long.class);
    }
}
