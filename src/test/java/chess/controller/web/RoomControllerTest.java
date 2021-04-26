package chess.controller.web;

import chess.domain.manager.ChessGameManager;
import chess.domain.manager.ChessGameManagerBundle;
import chess.domain.manager.ChessGameManagerFactory;
import chess.domain.piece.Rook;
import chess.service.ChessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RoomController.class)
class RoomControllerTest {
    private final static long CHESS_GAME_TEST_ID = 0;
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ChessService chessService;

    @Test
    @DisplayName("방 조회 테스트")
    void getGames() throws Exception {
        List<ChessGameManager> chessGameManagers = new ArrayList<>();
        ChessGameManager chessGameManager = ChessGameManagerFactory.createRunningGame(CHESS_GAME_TEST_ID, "temp");
        chessGameManagers.add(chessGameManager);

        given(chessService.findRunningGames()).willReturn(new ChessGameManagerBundle(chessGameManagers));

        mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.runningGames." + CHESS_GAME_TEST_ID)
                        .value("temp"));
    }

    @Test
    @DisplayName("새로운 게임 시작 테스트")
    void gameStart() throws Exception {
        ChessGameManager chessGameManager = ChessGameManagerFactory.createRunningGame(CHESS_GAME_TEST_ID, "test");
        Map<String, Object> data = new HashMap<>();
        data.put("title", "test");
        String json = new ObjectMapper().writeValueAsString(data);

        given(chessService.start("test")).willReturn(ChessGameManagerFactory.createRunningGame(CHESS_GAME_TEST_ID, "test"));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/room")
                .content(json).header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CHESS_GAME_TEST_ID))
                .andExpect(jsonPath("$.color").value("WHITE"))
                .andExpect(jsonPath("$.piecesAndPositions.size()").value(32));
    }

    @Test
    @DisplayName("게임 방 삭제 요청 테스트")
    void deleteRoom() throws Exception {
        mockMvc.perform(delete("/room/"+ CHESS_GAME_TEST_ID))
                .andExpect(status().isOk());
    }
}