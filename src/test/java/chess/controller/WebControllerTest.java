package chess.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import chess.model.domain.piece.Team;
import chess.service.ChessGameService;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class WebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChessGameService chessGameService;

    Map<Team, String> userNames = new HashMap<>();

    @BeforeEach
    void setUp() {
        userNames.put(Team.BLACK, "BLACK");
        userNames.put(Team.WHITE, "WHITE");
    }

    @Test
    void index() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"));
    }

    @Test
    void start() throws Exception {
        mockMvc.perform(post("/start")
            .param("roomId", "1"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("roomId", "1"))
            .andExpect(view().name("start"));
    }

    @Test
    void game() throws Exception {
        Integer gameId = chessGameService.saveNewGameInfo(userNames, 1);

        mockMvc.perform(post("/game")
            .param("roomId", "1"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("gameId", gameId + 1))
            .andExpect(view().name("game"));
    }

    @Test
    void load() throws Exception {
        Integer gameId = chessGameService.saveNewGameInfo(userNames, 1);

        mockMvc.perform(post("/load")
            .param("roomId", "1"))
            .andExpect(status().isOk())
            .andExpect(model().attribute("gameId", gameId))
            .andExpect(view().name("game"));
    }

    @Test
    void newGame() throws Exception {
        Integer gameId = chessGameService.saveNewGameInfo(userNames, 1);

        mockMvc.perform(post("/game/new")
            .param("gameId", String.valueOf(gameId)))
            .andExpect(status().isOk())
            .andExpect(model().attribute("gameId", gameId + 1))
            .andExpect(view().name("game"));
    }


    @Test
    void exitGame() throws Exception {
        mockMvc.perform(post("/game/exit"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"));
    }

    @Test
    void result() throws Exception {
        mockMvc.perform(post("/result"))
            .andExpect(status().isOk())
            .andExpect(view().name("result"));
    }
}