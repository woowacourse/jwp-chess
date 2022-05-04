package chess.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ChessGameControllerTest {

    @Autowired
    private ChessGameController chessGameController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @DisplayName("Http Method - GET /game/{gameId} 게임 가져오기")
    @Test
    void getGameByGameId() throws Exception {

        mockMvc.perform(get("/game/1"))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @DisplayName("Http Method - PUT game/{gameId}/ 체스 말 이동")
    @Test
    void move() throws Exception {
        Map<String, String> input = new HashMap<>();
        input.put("source", "a1");
        input.put("target", "a2");

        mockMvc.perform(put("/game/1/")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(input)))
            .andDo(print())
            .andExpect(status().is3xxRedirection());
    }

    @DisplayName("Http Method - get /game/ 게임 나가기")
    @Test
    void exitGame() throws Exception {
        mockMvc.perform(get("/game/"))
            .andDo(print())
            .andExpect(status().is3xxRedirection());
    }

}
