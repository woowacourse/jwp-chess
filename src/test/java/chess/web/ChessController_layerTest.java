package chess.web;

import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.piece.PieceFactory;
import chess.domain.piece.Position;
import chess.service.ChessService;
import chess.web.dto.GameDto;
import chess.web.dto.MessageDto;
import chess.web.dto.MoveDto;
import chess.web.dto.StatusDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ChessController.class)
class ChessController_layerTest {

    @MockBean
    private ChessService chessService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private ChessGame chessGame;

    @BeforeEach
    void setUp() {
        chessGame = new ChessGame(1L, new Board(PieceFactory.createPieces()));
        chessGame.start();
    }

    @Test
    void load() throws Exception {
        when(chessService.loadByGameId(1L))
                .thenReturn(new GameDto(chessGame));

        mockMvc.perform(get("/games/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value("running"));
    }

    @Test
    void move() throws Exception {
        chessGame.move(new Position(6, 0), new Position(5, 0));
        when(chessService.move(1L, "a2", "a3")).thenReturn(new GameDto(chessGame));

        mockMvc.perform(
                patch("/games/1/position")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new MoveDto("a2", "a3")
                        )))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pieceDtos[*].position").isArray())
                .andExpect(jsonPath("$.pieceDtos[*].position", hasItem("a3")));
    }

    @Test
    void status_returnGameScoresPerPlayers() throws Exception {
        when(chessService.getStatus(1L)).thenReturn(new StatusDto(1, 2));

        mockMvc.perform(get("/games/1/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("whiteScore").value(1.0))
                .andExpect(jsonPath("blackScore").value(2.0));
    }

    @Test
    void end() throws Exception {
        when(chessService.end(1L)).thenReturn(new MessageDto("finished"));

        mockMvc.perform(patch("/games/1/end"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("message").value("finished"));
    }
}