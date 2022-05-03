package chess.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.service.ChessGameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(ChessSpringRestController.class)
class ChessSpringRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChessGameService chessGameService;

    @Test
    @DisplayName("POST / : 20자 이하의 방제목, 비밀번호를 body로 받으면 201 상태코드 반환")
    void post_create() throws Exception {
        final String title = "abc";
        final String password = "123";
        final Map<String, String> body = new HashMap<>();
        body.put("title", title);
        body.put("password", password);

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST / : 20자 초과의 방제목, 비밀번호를 body로 받으면 400 상태코드 반환")
    void post_invalid_create() throws Exception {
        final String title = "abcdeabcdeabcdeabcdea";
        final String password = "123";
        final Map<String, String> body = new HashMap<>();
        body.put("title", title);
        body.put("password", password);

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PATCH /boards/{id}/move : 200 상태코드 반환")
    void patch_move() throws Exception {
        final String source = "a2";
        final String target = "a4";
        final Map<String, String> body = new HashMap<>();
        body.put("source", source);
        body.put("target", target);

        mockMvc.perform(patch("/boards/1/move")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /boards/{id}/restart : 200 상태코드 반환")
    void post_restart() throws Exception {
        mockMvc.perform(post("/boards/1/restart"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /boards/{id}/end : 200 상태코드 반환")
    void post_end() throws Exception {
        mockMvc.perform(post("/boards/1/end"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /boards/{id} : 200 상태코드 반환")
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/boards/1")
                .content("123"))
                .andExpect(status().isOk());
    }
}
