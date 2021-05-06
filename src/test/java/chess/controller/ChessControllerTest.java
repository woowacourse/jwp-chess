package chess.controller;

import chess.domain.player.Round;
import chess.service.ChessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ChessController.class)
public class ChessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChessService chessService;

    @DisplayName("홈 화면 정상 진입 테스트")
    @Test
    void homeTest() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"));
    }

    @DisplayName("체스 방 정상 진입 테스트")
    @Test
    void roomTest() throws Exception {
        given(chessService.getStoredRound(1))
                .willReturn(new Round());
        mockMvc.perform(get("/rooms/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("chess"));
    }
}
