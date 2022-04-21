package chess.service;

import chess.dto.request.RoomRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test")
@Transactional
@SpringBootTest
class ChessServiceTest {

    @Autowired
    private ChessService chessService;

    @DisplayName("")
    @Test
    void createRoom() {
        final RoomRequestDto roomRequestDto = new RoomRequestDto("체스 초보만");
        chessService.createRoom(roomRequestDto);
    }
}
