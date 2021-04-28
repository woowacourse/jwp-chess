package chess.controller;

import chess.service.ChessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ChessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChessService chessService;

    @Test
    @DisplayName("POST: /signup이 JSON으로 id와 password를 받으면, 정상 작동한다")
    void signup() throws Exception {
        mockMvc.perform(post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\" : \"" + "test" + "\"" + ", " +
                        "\"password\" : \"" + "test" + "\"" +
                        "}"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST: /signup이 JSON을 제대로 받지 않으면, 오류를 발생시킨다.")
    void signupException() throws Exception {
        mockMvc.perform(post("/signup"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("POST: /login에 제대로 정보를 넘기지 않으면, 오류를 발생시킨다.")
    void postLoginException() throws Exception {
        mockMvc.perform(post("/login"))
                .andExpect(status().is4xxClientError());
    }

    private MockHttpSession makeTestSession() {
        final MockHttpSession session = new MockHttpSession();
        session.setAttribute("id", "test");
        return session;
    }

    private void clearTestSession(MockHttpSession session) {
        session.clearAttributes();
    }

    @Test
    @DisplayName("GET: /login 요청 시 로그인 된 사용자 세션이 함께 보내지면, 200을 반환한다.")
    void getLoginStatus() throws Exception {
        final MockHttpSession session = makeTestSession();

        mockMvc.perform(get("/login")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        clearTestSession(session);
    }

    @Test
    @DisplayName("GET: /games가 정상 작동한다")
    void loadGameRooms() throws Exception {
        final MockHttpSession session = makeTestSession();

        mockMvc.perform(get("/games")
                .session(session))
                .andExpect(status().isOk());

        clearTestSession(session);
    }

    @Test
    @DisplayName("POST: /games가 정상 작동한다")
    void createNewGameRoom() throws Exception {
        final MockHttpSession session = makeTestSession();

        mockMvc.perform(post("/games")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\" : \"" + "testName" + "\"" + "}"))
                .andExpect(status().isOk());

        clearTestSession(session);
    }

    @Test
    @DisplayName("POST: /games/{id}가 정상 작동한다")
    void startNewGame() throws Exception {
        final MockHttpSession session = makeTestSession();

        mockMvc.perform(post("/games/1")
                .session(session))
                .andExpect(status().isOk());

        clearTestSession(session);
    }

    @Test
    @DisplayName("GET: /games/{id}가 정상 작동한다")
    void loadSavedGame() throws Exception {
        final MockHttpSession session = makeTestSession();

        mockMvc.perform(get("/games/1")
                .session(session))
                .andExpect(status().isOk());

        clearTestSession(session);
    }

    @Test
    @DisplayName("DELETE: /games/{id}가 정상 작동한다")
    void deleteGame() throws Exception {
        final MockHttpSession session = makeTestSession();

        mockMvc.perform(delete("/games/1")
                .session(session))
                .andExpect(status().isOk());

        clearTestSession(session);
    }

    @Test
    @DisplayName("POST: /games/saved은 허용하지 않는다")
    void loadSavedGamePostRequestError() throws Exception {
        final MockHttpSession session = makeTestSession();

        mockMvc.perform(post("/games/saved")
                .session(session))
                .andExpect(status().is4xxClientError());

        clearTestSession(session);
    }

    @Test
    @DisplayName("{start: e2, destination: e4}를 엔티티 바디로 추가해 POST 방식으로 데이터가 넘어가면, 상태코드 200을 반환한다")
    void move() throws Exception {
        final MockHttpSession session = makeTestSession();

        mockMvc.perform(post("/games/1/move")
                .session(session)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"start\" : \"" + "e2" + "\"" + ", " +
                        "\"destination\" : \"" + "e4" + "\"" +
                        "}"))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("엔티티 바디가 없이 POST 방식으로 요청하면, 400번대 에러를 반환한다.")
    void moveWithoutBody() throws Exception {
        final MockHttpSession session = makeTestSession();

        mockMvc.perform(post("/games/1/move")
                .session(session))
                .andExpect(status().is4xxClientError());

        clearTestSession(session);
    }

    @Test
    @DisplayName("GET: /games/move는 허용하지 않는다")
    void moveGetRequest() throws Exception {
        final MockHttpSession session = makeTestSession();

        mockMvc.perform(get("/games/1/move")
                .session(session))
                .andExpect(status().is4xxClientError());

        clearTestSession(session);
    }
}
