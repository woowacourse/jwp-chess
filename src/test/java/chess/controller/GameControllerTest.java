package chess.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.dto.MoveDto;
import chess.dto.TitleDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private String id;

    @BeforeEach
    void setUp() throws Exception {
        String content = objectMapper.writeValueAsString(new TitleDto("test-room"));

        MvcResult mvcResult = mockMvc.perform(post("/game")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON))
            .andReturn();
        id = mvcResult.getResponse().getContentAsString();
    }

    @Test
    void loadGame() throws Exception {
        mockMvc.perform(get("/room/" + id + "/game-info"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(id));
    }

    @Test
    void move() throws Exception {
        String content = objectMapper.writeValueAsString(new MoveDto("b2", "b3"));

        mockMvc.perform(put("/room/" + id + "/move")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(id));
    }

    @Test
    void finish() throws Exception {
        mockMvc.perform(post("/room/" + id + "/finish"))
            .andExpect(status().isOk());
    }

    @Test
    void restart() throws Exception {
        mockMvc.perform(post("/room/" + id + "/restart"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(id))
            .andExpect(jsonPath("finished").value(false));
    }
}
