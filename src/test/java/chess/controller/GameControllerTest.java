package chess.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.controller.api.GameController;
import chess.domain.board.ChessBoard;
import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.dto.MoveDto;
import chess.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = GameController.class)
public class GameControllerTest {

    @MockBean
    private GameService gameService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void loadGame() throws Exception {
        when(gameService.loadGame(1L))
            .thenReturn(new ChessGame(1L, Color.WHITE, false, new ChessBoard(), "test-room"));

        mockMvc.perform(get("/room/1/game-info"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(1));
    }

    @Test
    void move() throws Exception {
        String content = objectMapper.writeValueAsString(new MoveDto("b2", "b3"));

        when(gameService.loadGame(1L))
            .thenReturn(new ChessGame(1L, Color.WHITE, false, new ChessBoard(), "test-room"));

        mockMvc.perform(put("/room/1/move")
            .content(content)
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(1));
    }

    @Test
    void finish() throws Exception {
        when(gameService.loadGame(1L))
            .thenReturn(new ChessGame(1L, Color.WHITE, false, new ChessBoard(), "test-room"));

        mockMvc.perform(post("/room/1/finish"))
            .andExpect(status().isOk());
    }

    @Test
    void restart() throws Exception {
        when(gameService.restart(1L))
            .thenReturn(new ChessGame(1L, Color.BLACK, false, new ChessBoard(), "test-room"));

        mockMvc.perform(post("/room/1/restart"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(1))
            .andExpect(jsonPath("finished").value(false))
            .andExpect(jsonPath("turn").value("BLACK"));
    }
}
