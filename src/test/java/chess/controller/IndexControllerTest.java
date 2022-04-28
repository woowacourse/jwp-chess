package chess.controller;

import chess.service.ChessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(IndexController.class)
public class IndexControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean(ChessService.class)
    ChessService chessService;

    @Test
    public void returnIndex() throws Exception {

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    public void returnNewGame() throws Exception {

        mvc.perform(get("/newgame"))
                .andExpect(status().isOk())
                .andExpect(view().name("newgame"));
    }

    @Test
    public void createAndReturnIndex() throws Exception {
        mvc.perform(get("/create")
                        .param("gameID", "test")
                        .param("gamePW", "test"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/"));
    }

    @Test
    public void returnDelete() throws Exception {

        mvc.perform(get("/checkPW")
                        .param("gameCode", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("delete"));
    }

    @Test
    public void deleteAndReturnIndex() throws Exception {

        mvc.perform(get("/delete")
                        .param("gameID", "test")
                        .param("inputPW", "test"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/"));
    }
}
