package chess.controller;

import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class HomeControllerTest {

    @Test
    void lobby() throws Exception {
        standaloneSetup(new HomeController()).build()
                .perform(get("/"))
                .andExpect(status().isOk());
    }

    @Test
    void initBoard() throws Exception {
        standaloneSetup(new HomeController()).build()
                .perform(get("/game/{id}", "1"))
                .andExpect(status().isOk());
    }
}