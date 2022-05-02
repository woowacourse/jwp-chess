package chess.controller;

import chess.controller.dto.request.CreateGameRequest;
import chess.controller.dto.request.MoveRequest;
import chess.service.ChessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.StringContains;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ChessController.class)
public class ChessControllerArgumentsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChessService chessService;

    @DisplayName("게임 생성 요청값으로 null 또는 빈 값이 들어올 수 없다.")
    @ParameterizedTest
    @CsvSource(value = {" , ", "' ',' '"})
    void invalid_Create_Request(String gameName, String password) throws Exception {
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName, password);

        mockMvc.perform(MockMvcRequestBuilders.post("/games")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(createGameRequest)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        StringContains.containsString("게임 이름은 빈 값일 수 없습니다.")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        StringContains.containsString("비밀번호는 빈 값일 수 없습니다.")));
    }

    @DisplayName("기물 이동 요청값으로 null이 들어올 수 없다.")
    @Test
    void invalid_Move_Request() throws Exception {
        MoveRequest moveRequest = new MoveRequest(null, null);

        mockMvc.perform(MockMvcRequestBuilders.patch("/games/1/pieces")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(moveRequest)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        StringContains.containsString("이동할 말의 위치는 NULL 값일 수 없습니다.")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        StringContains.containsString("이동할 목표 위치는 NULL 값일 수 없습니다.")));
    }
}
