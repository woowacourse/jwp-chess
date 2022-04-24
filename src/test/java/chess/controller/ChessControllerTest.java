package chess.controller;

import static chess.controller.ControllerTestFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import chess.config.MockMvcConfig;
import chess.domain.board.BoardFactory;
import chess.domain.game.Score;
import chess.dto.BoardsDto;
import chess.dto.request.RoomRequestDto;
import chess.dto.response.*;
import chess.entity.BoardEntity;
import chess.entity.RoomEntity;
import chess.service.ChessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;

@Import(MockMvcConfig.class)
@ActiveProfiles("test")
@WebMvcTest(ChessController.class)
class ChessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChessService chessService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("방을 생성하면 201 create와 Location을 헤더로 반환한다.")
    @Test
    void create() throws Exception {
        final RoomRequestDto roomRequestDto = new RoomRequestDto(ROOM_NAME);
        String content = objectMapper.writeValueAsString(roomRequestDto);

        given(chessService.createRoom(any()))
            .willReturn(RoomResponseDto.of(createRoomEntity(1L)));
        mockMvc.perform(post(DEFAULT_API)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/chess/rooms/1"));
    }

    @DisplayName("진행 중인 모든 방을 조회하고 200 ok 를 반환한다.")
    @Test
    void findRooms() throws Exception {
        RoomsResponseDto roomsResponseDto = RoomsResponseDto.of(List.of(createRoomEntity(1L)
                , createRoomEntity(2L)));
        String response = objectMapper.writeValueAsString(roomsResponseDto);

        given(chessService.findRooms())
                .willReturn(roomsResponseDto);
        mockMvc.perform(get(DEFAULT_API))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @DisplayName("진행 중인 방을 들어가면 200 ok와 gameResponseDto를 반환한다.")
    @Test
    void enterRoom() throws Exception {
        GameResponseDto gameResponseDto = GameResponseDto.of(createRoomEntity(1L)
                , BoardsDto.of(createBoardEntities()));
        String response = objectMapper.writeValueAsString(gameResponseDto);

        given(chessService.enterRoom(any()))
                .willReturn(gameResponseDto);
        mockMvc.perform(get(DEFAULT_API + "/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @DisplayName("종료된 방을 들어가면 400 에러가 발생한다.")
    @Test
    void enterRoomException() throws Exception {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ERROR_FINISHED);
        String response = objectMapper.writeValueAsString(errorResponseDto);

        given(chessService.enterRoom(any()))
                .willThrow(new IllegalArgumentException(ERROR_FINISHED));

        mockMvc.perform(get(DEFAULT_API + "/1"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(response));
    }

    @DisplayName("게임을 종료하면 200 ok와 statusResponseDto를 반환한다.")
    @Test
    void finishGame() throws Exception {
        doNothing().when(chessService).endRoom(any());
        StatusResponseDto statusResponseDto = StatusResponseDto.of(new Score(BoardFactory.initialize()));
        String response = objectMapper.writeValueAsString(statusResponseDto);

        given(chessService.createStatus(any()))
                .willReturn(statusResponseDto);
        mockMvc.perform(patch(DEFAULT_API + "/1/end"))
                .andExpect(status().isOk())
                .andExpect(content().string(response));
    }

    @DisplayName("종료된 방을 다시 종료하면 400 에러가 발생한다.")
    @Test
    void finishGameException() throws Exception {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(ERROR_FINISHED);
        String response = objectMapper.writeValueAsString(errorResponseDto);

        doThrow(new IllegalArgumentException(ERROR_FINISHED)).when(chessService).endRoom(1L);

        mockMvc.perform(patch(DEFAULT_API + "/1/end"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(response));
    }

    private RoomEntity createRoomEntity(Long id) {
        return new RoomEntity(id, ROOM_NAME, WHITE, FALSE);
    }

    private List<BoardEntity> createBoardEntities() {
        return BoardFactory.initialize()
                .entrySet()
                .stream()
                .map(entry -> new BoardEntity(
                        1L,
                        entry.getKey().convertPositionToString(),
                        entry.getValue().convertPieceToString())
                )
                .collect(Collectors.toList());
    }
}
