package chess.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.MockChessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class SpringChessControllerTest {

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new SpringChessController(new MockChessService()))
            .build();
    }

    @DisplayName("\"/\" 경로에는 lobby.html이 반환된다.")
    @Test
    void lobby() throws Exception {
        mockMvc.perform(get("/").accept(MediaType.TEXT_HTML_VALUE))
            .andDo(print())
            .andExpect(status().isOk());
    }

    @DisplayName("\"/room/1\" 경로에는 index.html이 반환된다.")
    @Test
    void joinRoom() throws Exception {
        mockMvc.perform(get("/rooms/1123"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("roomId"))
            .andExpect(model().attributeExists("board"))
            .andExpect(model().attributeExists("userInfo"));
    }
}
