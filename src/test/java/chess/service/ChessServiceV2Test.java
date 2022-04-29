package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dao.RoomFakeDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ChessServiceV2Test {

    private final ChessServiceV2 chessServiceV2 = new ChessServiceV2(new RoomFakeDao());

    @Test
    @DisplayName("한개의 방을 생성할 수 있다.")
    void insertRoom() {
        final Long roomId = chessServiceV2.insertRoom("title1", "1111");

        assertThat(roomId).isInstanceOf(Long.class);
    }
}
