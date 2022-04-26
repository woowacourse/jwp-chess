package chess.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(WebController.class)
class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("초기 화면 테스트")
    @Test
    void index() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/")
                        .accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(MockMvcResultMatchers.view().name("index.html"));
    }

    @DisplayName("게임 방 입장 테스트")
    @Test
    void enterGame() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/game/1")
                        .accept(MediaType.TEXT_HTML_VALUE))
                .andExpect(MockMvcResultMatchers.view().name("/game.html"));
    }
}
