package chess.service;

import static chess.controller.ControllerTestFixture.ROOM_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.dto.request.RoomAccessRequestDto;
import chess.dto.response.StatusResponseDto;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomRequestDto;
import chess.dto.response.GameResponseDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.RoomsResponseDto;
import chess.entity.BoardEntity;
import chess.repository.BoardRepository;
import chess.repository.RoomRepository;
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

    @Autowired
    private RoomRepository roomRepository;

    @DisplayName("체스 초보만이라는 이름을 가진 방을 생성한다.")
    @Test
    void createRoom() {
        final RoomResponseDto room = createTestRoom("체스 초보만");
        assertAll(
                () -> assertThat(room.getName()).isEqualTo("체스 초보만"),
                () -> assertThat(boardRepository.findBoardByRoomId(room.getId())).hasSize(64)
        );
    }

    @DisplayName("생성한 체스 방을 모두 가져온다.")
    @Test
    void findRooms() {
        createTestRoom("체스 초보만1");
        createTestRoom("체스 초보만2");

        final RoomsResponseDto roomsResponseDto = chessService.findRooms();

        assertThat(roomsResponseDto.getRoomResponseDtos()).hasSize(2);
    }

    @DisplayName("체스 초보만 방에 입장한다.")
    @Test
    void enterRoom() {
        final Long id = createTestRoom("체스 초보만").getId();
        final GameResponseDto gameResponseDto = chessService.enterRoom(id);

        assertAll(
            () -> assertThat(gameResponseDto.getName()).isEqualTo("체스 초보만"),
            () -> assertThat(gameResponseDto.getTeam()).isEqualTo("white"),
            () -> assertThat(gameResponseDto.getBoard().getBoards()).hasSize(64)
        );
    }

    @DisplayName("종료된 방에 입장을 요청하여 에러가 발생한다.")
    @Test
    void enterRoomException() {
        final Long id = createTestRoom("체스 초보만").getId();
        chessService.endRoom(id, new RoomAccessRequestDto(ROOM_PASSWORD));

        assertThatThrownBy(() -> chessService.enterRoom(id))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 이미 종료된 게임입니다.");
    }

    @DisplayName("비밀번호가 맞지 않은 경우 입장에 실패한다.")
    @Test
    void enterRoomNotPasswordException() {
        final Long id = createTestRoom("체스 초보만").getId();

        assertThatThrownBy(() -> chessService.endRoom(id, new RoomAccessRequestDto("123123")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비밀번호가 틀렸습니다.");
    }

    @DisplayName("a2의 기물을 a4로 이동한다.")
    @Test
    void move() {
        final Long id = createTestRoom("체스 초보만").getId();
        chessService.move(id, new MoveRequestDto("a2", "a4"));
        final BoardEntity sourceBoardEntity = boardRepository.findBoardByRoomIdAndPosition(id, "a2");
        final BoardEntity targetBoardEntity = boardRepository.findBoardByRoomIdAndPosition(id, "a4");

        assertAll(
            () -> assertThat(sourceBoardEntity.getPiece()).isEqualTo("blank"),
            () -> assertThat(targetBoardEntity.getPiece()).isEqualTo("white_pawn")
        );
    }

    @DisplayName("종료된 방에 이동을 요청하여 에러가 발생한다.")
    @Test
    void moveException() {
        final Long id = createTestRoom("체스 초보만").getId();
        chessService.endRoom(id, new RoomAccessRequestDto(ROOM_PASSWORD));

        assertThatThrownBy(() -> chessService.move(id, new MoveRequestDto("a2", "a4")))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 이미 종료된 게임입니다.");
    }

    @DisplayName("현재 방의 체스 게임을 종료한다.")
    @Test
    void end() {
        final Long id = createTestRoom("체스 초보만").getId();
        chessService.endRoom(id, new RoomAccessRequestDto(ROOM_PASSWORD));
        final RoomsResponseDto rooms = chessService.findRooms();
        assertThat(rooms.getRoomResponseDtos()).isEmpty();
    }

    @DisplayName("종료된 방에 다시 종료를 요청하여 에러가 발생한다.")
    @Test
    void endException() {
        final Long id = createTestRoom("체스 초보만").getId();
        chessService.endRoom(id, new RoomAccessRequestDto(ROOM_PASSWORD));

        assertThatThrownBy(() -> chessService.endRoom(id, new RoomAccessRequestDto(ROOM_PASSWORD)))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("[ERROR] 이미 종료된 게임입니다.");
    }

    @DisplayName("비밀번호가 맞지 않은 경우 종료에 실패한다.")
    @Test
    void endNotPasswordException() {
        final Long id = createTestRoom("체스 초보만").getId();

        assertThatThrownBy(() -> chessService.endRoom(id, new RoomAccessRequestDto("123123")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비밀번호가 틀렸습니다.");
    }

    @DisplayName("최초 방생성시 score를 확인한다.")
    @Test
    void createStatus() {
        final Long id = createTestRoom("체스 초보만").getId();
        final StatusResponseDto status = chessService.createStatus(id);

        assertThat(status.getBlackScore()).isEqualTo(38);
    }

    @DisplayName("방 이름을 업데이트 한다.")
    @Test
    void updateRoomName() {
        final Long id = createTestRoom("체스 초보만").getId();
        chessService.updateRoomName(id, new RoomRequestDto("체스 왕 초보만", "1234"));

        assertThat(roomRepository.findById(id).getName()).isEqualTo("체스 왕 초보만");
    }


    @DisplayName("종료된 방 이름을 업데이트 하여 에러를 발생한다.")
    @Test
    void updateRoomNameException() {
        final Long id = createTestRoom("체스 초보만").getId();
        chessService.endRoom(id, new RoomAccessRequestDto(ROOM_PASSWORD));
        assertThatThrownBy(() -> chessService.updateRoomName(id, new RoomRequestDto("체스 왕 초보만", "1234")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이미 종료된 게임입니다.");
    }

    @DisplayName("비밀번호가 맞지 않은 경우 업데이트에 실패한다.")
    @Test
    void updateNotPasswordException() {
        final Long id = createTestRoom("체스 초보만").getId();

        assertThatThrownBy(() -> chessService.updateRoomName(id, new RoomRequestDto("체스 개초보만", "123123")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비밀번호가 틀렸습니다.");
    }

    private RoomResponseDto createTestRoom(final String roomName) {
        final RoomRequestDto roomRequestDto = new RoomRequestDto(roomName, ROOM_PASSWORD);
        return chessService.createRoom(roomRequestDto);
    }
}
