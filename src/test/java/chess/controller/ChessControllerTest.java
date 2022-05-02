package chess.controller;

import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import chess.domain.board.ChessBoard;
import chess.domain.board.ChessBoardGenerator;
import chess.domain.board.ChessGame;
import chess.dto.GameRoomResponse;
import chess.service.ChessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChessGameController.class)
public class ChessControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ChessService chessService;

    @Test
    void showLobby() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("games"))
                .andExpect(view().name("lobby"));
    }

    @DisplayName("새로운 GameID를 이용해 ChessGameRoom으로 리다이렉트한다")
    @Test
    void redirectToNewGame() throws Exception {
        mockMvc
                .perform(post("/chess/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "testGame")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection());
    }

    @DisplayName("ChessGame 화면을 출력한다")
    @Test
    void showChessGameRoom() throws Exception {
        GameRoomResponse response = new GameRoomResponse("1", "testGame");

        given(chessService.findGameById(1))
                .willReturn(response);

        mockMvc
                .perform(get("/chess/game/1"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("chessGameRoom"))
                .andExpect(view().name("game"));
    }

    @DisplayName("저장된 게임을 불러온다")
    @Test
    void loadGames() throws Exception {

        ChessGame testGame = new ChessGame("1", "testGame", "1234", false,
                new ChessBoard(new ChessBoardGenerator()));

        given(chessService.loadSavedGame(anyLong()))
                .willReturn(testGame);

        mockMvc
                .perform(get("/chess/game/1/board"))
                .andExpect(status().isOk());
    }
}
