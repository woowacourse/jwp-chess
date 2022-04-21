package chess.service;

import static org.assertj.core.api.Assertions.assertThat;

import chess.dto.request.RoomRequestDto;
import chess.dto.response.RoomResponseDto;
import chess.repository.BoardRepository;
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

    @Autowired
    private BoardRepository boardRepository;

    @DisplayName("체스 초보만이라는 이름을 가진 방을 생성한다.")
    @Test
    void createRoom() {
        final RoomRequestDto roomRequestDto = new RoomRequestDto("체스 초보만");
        final RoomResponseDto room = chessService.createRoom(roomRequestDto);

        assertThat(room.getName()).isEqualTo("체스 초보만");
        assertThat(boardRepository.findBoardByRoomId(room.getId())).hasSize(64);
    }

    @DisplayName("")
    @Test
    void enterRoom() {
        final RoomRequestDto roomRequestDto = new RoomRequestDto("체스 초보만");
        final Long id = chessService.createRoom(roomRequestDto).getId();

    }
}
