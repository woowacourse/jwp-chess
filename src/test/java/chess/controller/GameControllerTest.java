package chess.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import chess.controller.api.GameController;
import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.dto.ChessGameDto;
import chess.dto.MoveDto;
import chess.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("id로 게임 불러오기")
    void loadGame() throws Exception {
        ChessGame chessGame = new ChessGame(1L, Color.WHITE, false, new ChessBoard(), "test-game");
        ChessGameDto chessGameDto = new ChessGameDto(chessGame);
        when(gameService.loadGame(1L)).thenReturn(chessGame);

        mockMvc.perform(get("/room/1/game-info"))
            .andExpect(status().isOk())
            .andExpect(content().json(objectMapper.writeValueAsString(chessGameDto)));
    }

    @Test
    @DisplayName("움직일 수 있는 경우")
    void moveSuccess() throws Exception {
        MoveDto moveDto = new MoveDto("a2", "a3");
        ChessGame chessGame = new ChessGame(1L, Color.WHITE, false, new ChessBoard(), "test-game");
        chessGame.move(Position.of(moveDto.getSource()), Position.of(moveDto.getTarget()));
        when(gameService.loadGame(1L))
            .thenReturn(chessGame);

        mockMvc.perform(put("/room/1/move")
            .content(objectMapper.writeValueAsString(moveDto))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(
                content().json(objectMapper.writeValueAsString(new ChessGameDto(chessGame))));
    }

    @Test
    @DisplayName("움직일 수 없는 경우 에러와 함께 bad request")
    void moveFailed() throws Exception {
        doThrow(new IllegalArgumentException()).when(gameService).move(eq(1L), any());

        mockMvc.perform(put("/room/1/move")
            .content(objectMapper.writeValueAsString(new MoveDto("b1", "b2")))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("특정한 방의 게임 수동으로 종료시키기")
    void finish() throws Exception {
        when(gameService.loadGame(1L))
            .thenReturn(new ChessGame(1L, Color.WHITE, false, new ChessBoard(), "test-room"));

        mockMvc.perform(post("/room/1/finish"))
            .andExpect(status().isOk());

        verify(gameService, times(1))
            .finish(1L);
    }

    @Test
    @DisplayName("특정한 방의 게임 재시작하기")
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
