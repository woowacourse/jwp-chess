package chess.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import chess.domain.board.ChessBoard;
import chess.domain.board.ChessBoardGenerator;
import chess.domain.board.ChessGame;
import chess.dto.BoardResponse;
import chess.dto.GameCreationRequest;
import chess.dto.GameRoomResponse;
import chess.service.ChessService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
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

    @DisplayName("새로운 GameID를 이용해 게임을 생성한 후 루트로 리다이렉트한다")
    @Test
    void redirectToNewGame() throws Exception {
        given(chessService.addChessGame(any(GameCreationRequest.class)))
                .willReturn(1L);

        mockMvc
                .perform(post("/chess/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "test")
                        .param("password", "1234"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string(HttpHeaders.LOCATION, "/"));
    }

    @DisplayName("ChessGame 화면을 출력한다")
    @Test
    void showChessGameRoom() throws Exception {
        GameRoomResponse response = new GameRoomResponse("1", "testGame");

        given(chessService.findRoomToEnter(1, ""))
                .willReturn(response);

        mockMvc
                .perform(post("/chess/game/1")
                        .param("password", ""))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("chessGameRoom"))
                .andExpect(view().name("game"));
    }

    @DisplayName("저장된 게임을 불러온다")
    @Test
    void loadGames() throws Exception {
        ChessGame testGame = new ChessGame("1", "testGame", "1234", false,
                new ChessBoard(new ChessBoardGenerator()));
        BoardResponse boardResponse = new BoardResponse(testGame);

        given(chessService.loadSavedBoard(anyLong()))
                .willReturn(boardResponse);

        mockMvc
                .perform(get("/chess/game/1/board"))
                .andExpect(status().isOk());
    }
}
