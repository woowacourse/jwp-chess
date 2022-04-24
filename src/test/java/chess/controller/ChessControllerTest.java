package chess.controller;

import static chess.controller.ControllerTestFixture.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import chess.config.MockMvcConfig;
import chess.dto.request.RoomRequestDto;
import chess.dto.response.RoomResponseDto;
import chess.dto.response.RoomsResponseDto;
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

    @DisplayName("방을 생성하는 기능을 수행한다.")
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

    @DisplayName("진행 중인 모든 방을 찾아온다.")
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

    private RoomEntity createRoomEntity(Long id) {
        return new RoomEntity(id, ROOM_NAME, WHITE, FALSE);
    }
}
