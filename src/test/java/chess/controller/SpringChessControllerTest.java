package chess.controller;

import chess.service.SpringChessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpringChessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpringChessService springChessService;

    @Test
    @DisplayName("GET: /games/new가 정상 작동한다")
    void startNewGame() throws Exception {
        mockMvc.perform(get("/games/new"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST: /games/new은 허용하지 않는다")
    void startNewGamePostRequestError() throws Exception {
        mockMvc.perform(post("/games/new"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("GET: /games/saved가 정상 작동한다")
    void loadSavedGame() throws Exception {
        mockMvc.perform(get("/games/saved"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST: /games/saved은 허용하지 않는다")
    void loadSavedGamePostRequestError() throws Exception {
        mockMvc.perform(post("/games/saved"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("{start: e2, destination: e4}를 엔티티 바디로 추가해 POST 방식으로 데이터가 넘어가면, 상태코드 200을 반환한다")
    void move() throws Exception {
        mockMvc.perform(post("/games/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"start\" : \"" + "e2" + "\"" + ", " +
                        "\"destination\" : \"" + "e4" + "\"" +
                        "}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("엔티티 바디가 없이 POST 방식으로 요청하면, 400번대 에러를 반환한다.")
    void moveWithoutBody() throws Exception {
        mockMvc.perform(post("/games/move"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("GET: /games/move는 허용하지 않는다")
    void moveGetRequest() throws Exception {
        mockMvc.perform(get("/games/move"))
                .andExpect(status().is4xxClientError());
    }
}
