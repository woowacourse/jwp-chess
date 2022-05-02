package chess.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import chess.config.HandlebarConfig;
import chess.domain.Score;
import chess.domain.piece.Color;
import chess.dto.ChessGameDto;
import chess.dto.GameStatus;
import chess.exception.ChessGameException;
import chess.service.ChessGameService;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@WebMvcTest(ChessGameController.class)
@ContextConfiguration(classes = HandlebarConfig.class)
class ChessGameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChessGameService chessGameService;

    @Test
    @DisplayName("체스 게임 방 접속")
    void chessGame() throws Exception {
        Mockito.when(chessGameService.findChessGame(1))
            .thenReturn(new ChessGameDto(1, "hoho", GameStatus.RUNNING, new Score(), new Score(), Color.WHITE));
        Mockito.when(chessGameService.findPieces(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/chess-game/{id}", 1))
            .andDo(print())
            .andExpectAll(
                status().isOk(),
                view().name("chess-game"),
                model().attributeExists("pieces"),
                model().attributeExists("chessGame")
            );
    }

    @Test
    @DisplayName("체스 게임 방 접속")
    void notExistsChessGameId() throws Exception {
        Mockito.when(chessGameService.findChessGame(1))
            .thenThrow(new IllegalArgumentException("해당하는 체스 게임이 존재하지 않습니다."));
        Mockito.when(chessGameService.findPieces(1)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/chess-game/{id}", 1))
            .andDo(print())
            .andExpectAll(
                status().is3xxRedirection(),
                redirectedUrl("/error"),
                flash().attributeExists("hasError"),
                flash().attributeExists("errorMessage")
            );
    }

    @Test
    @DisplayName("정상적인 기물 이동")
    void move() throws Exception {
        Mockito.when(chessGameService.move(any()))
            .thenReturn(new ChessGameDto(1, "hoho", GameStatus.RUNNING, new Score(), new Score(), Color.WHITE));

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "1");
        params.add("from", "A2");
        params.add("to", "A4");

        mockMvc.perform(post("/chess-game/move")
            .params(params))
            .andDo(print())
            .andExpectAll(
                status().is3xxRedirection(),
                redirectedUrl("/chess-game/" + 1)
            );
    }

    @Test
    @DisplayName("체스 게임 생성")
    void createChessGame() throws Exception {
        mockMvc.perform(post("/chess-game")
            .param("name", "hoho"))
            .andDo(print())
            .andExpectAll(
                redirectedUrl("/")
            );
    }

    @Test
    @DisplayName("비정상적인 기물 이동")
    void invalidMove() throws Exception {
        Mockito.when(chessGameService.move(any()))
            .thenThrow(new ChessGameException(1, "기물을 A2에서 A5로 이동할 수 없습니다."));

        mockMvc.perform(post("/chess-game/move")
            .param("id", String.valueOf(1))
            .param("from", "A2")
            .param("to", "A5"))
            .andDo(print())
            .andExpectAll(
                status().is3xxRedirection(),
                redirectedUrl("/chess-game?id=" + 1),
                flash().attributeExists("hasError"),
                flash().attributeExists("errorMessage")
            );
    }
}
