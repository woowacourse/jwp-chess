package chess.controller;

import chess.dto.MoveRequest;
import chess.service.ChessGameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
@AutoConfigureMockMvc
class ChessRestControllerMockMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChessGameService chessGameService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("게임 목록 불러오기 [GET] /games의 정상 작동")
    @Test
    void requestGamesTest() throws Exception {
        this.mockMvc.perform(get("/games"))
                .andExpect(status().isOk());
    }

    @DisplayName("기물을 이동요청 API 테스트")
    @Test
    void movePieceTest() throws Exception {
        String content = objectMapper.writeValueAsString(new MoveRequest(1, "a2", "a4"));

        this.mockMvc.perform(put("/games/1/pieces")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk());
    }

    @DisplayName("특정 게임 불러오기 요청 API 테스트")
    @Test
    void loadGameTest() throws Exception {
        this.mockMvc.perform(get("/games/1"))
                .andExpect(status().isOk());
    }

}