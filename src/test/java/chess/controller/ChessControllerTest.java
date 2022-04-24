package chess.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.dto.request.RoomRequestDto;
import chess.dto.response.RoomResponseDto;
import chess.entity.RoomEntity;
import chess.service.ChessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
        final RoomRequestDto roomRequestDto = new RoomRequestDto("체스 초보만");
        String content = objectMapper.writeValueAsString(roomRequestDto);

        BDDMockito.given(chessService.createRoom(any()))
            .willReturn(RoomResponseDto.of(new RoomEntity(1L, "체스 초보만", "white", false)));
        mockMvc.perform(post("/api/chess/rooms")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", "/api/chess/rooms/1"));
    }
}
