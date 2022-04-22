package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomRequestDto;
import chess.dto.response.GameResponseDto;
import chess.dto.response.RoomResponseDto;
import chess.entity.BoardEntity;
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
        final RoomResponseDto room = createTestRoom();
        assertThat(room.getName()).isEqualTo("체스 초보만");
        assertThat(boardRepository.findBoardByRoomId(room.getId())).hasSize(64);
    }

    @DisplayName("체스 초보만 방에 입장한다.")
    @Test
    void enterRoom() {
        final Long id = createTestRoom().getId();
        final GameResponseDto gameResponseDto = chessService.enterRoom(id);

        assertAll(
            () -> assertThat(gameResponseDto.getName()).isEqualTo("체스 초보만"),
            () -> assertThat(gameResponseDto.getTeam()).isEqualTo("white"),
            () -> assertThat(gameResponseDto.getBoard().getBoards()).hasSize(64)
        );
    }

    @DisplayName("a2의 기물을 a4로 이동한다.")
    @Test
    void move() {
        final Long id = createTestRoom().getId();
        chessService.move(id, new MoveRequestDto("a2", "a4"));
        final BoardEntity sourceBoardEntity = boardRepository.findBoardByRoomIdAndPosition(id, "a2");
        final BoardEntity targetBoardEntity = boardRepository.findBoardByRoomIdAndPosition(id, "a4");

        assertAll(
            () -> assertThat(sourceBoardEntity.getPiece()).isEqualTo("blank"),
            () -> assertThat(targetBoardEntity.getPiece()).isEqualTo("white_pawn")
        );
    }

    private RoomResponseDto createTestRoom() {
        final RoomRequestDto roomRequestDto = new RoomRequestDto("체스 초보만");
        return chessService.createRoom(roomRequestDto);
    }
}
