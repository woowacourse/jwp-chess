package chess.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.domain.piece.StartedPawn;
import chess.domain.piece.position.Position;
import chess.domain.piece.property.Color;
import chess.web.dao.ChessBoardDao;
import chess.web.dao.PlayerDao;
import chess.web.dto.MoveDto;
import chess.web.service.ChessGameService;
import chess.web.service.fakedao.FakeChessBoardDao;
import chess.web.service.fakedao.FakePlayerDao;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class ChessGameControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    ChessBoardDao chessBoardDao = new FakeChessBoardDao();
    PlayerDao playerDao = new FakePlayerDao();
    ChessGameService chessGameService = new ChessGameService(chessBoardDao, playerDao);

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(
                new ChessGameController(chessGameService),
                new ChessGameRestController(chessGameService))
                .build();
    }

    @Test
    void getRoot() throws Exception {
        this.mockMvc.perform(get("/").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @Test
    void getStart() throws Exception {
        this.mockMvc.perform(get("/start").accept(MediaType.TEXT_HTML))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/play"));
    }

    @Test
    void getPlay() throws Exception {
        this.mockMvc.perform(get("/play").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }

    @Test
    void postMove() throws Exception {
        chessBoardDao.save(Position.of("a2"), new StartedPawn(Color.WHITE));
        String content = objectMapper.writeValueAsString(new MoveDto("a2", "a4"));

        this.mockMvc.perform(post("/move")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getEnd() throws Exception {
        this.mockMvc.perform(get("/end").accept(MediaType.TEXT_HTML))
                .andExpect(status().isOk());
    }
}
