package chess.controller.web;

import chess.controller.web.dto.MoveRequestDto;
import chess.domain.manager.ChessGameManager;
import chess.domain.manager.ChessGameManagerBundle;
import chess.domain.manager.ChessGameManagerFactory;
import chess.domain.piece.attribute.Color;
import chess.domain.statistics.ChessGameStatistics;
import chess.service.ChessService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import net.minidev.json.JSONObject;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChessController.class)
class ChessControllerTest {
    private final static long CHESS_GAME_TEST_ID = 0;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ChessService chessService;

    @Test
    @DisplayName("게임 점수 조회 테스트")
    void getScore() throws Exception {
        ChessGameStatistics chessGameStatistics = ChessGameStatistics.createNotStartGameResult();

        given(chessService.getStatistics(CHESS_GAME_TEST_ID)).willReturn(chessGameStatistics);

        mockMvc.perform(get("/games/" + CHESS_GAME_TEST_ID + "/score"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matchResult").value("무승부"))
                .andExpect(jsonPath("$.colorsScore.size()").value(2));
    }

    @Test
    @DisplayName("해당 게임 로딩 테스트")
    void loadGame() throws Exception {
        ChessGameManager chessGameManager = ChessGameManagerFactory.createRunningGame(CHESS_GAME_TEST_ID, "");

        given(chessService.findById(CHESS_GAME_TEST_ID)).willReturn(chessGameManager);

        mockMvc.perform(get("/games/" + CHESS_GAME_TEST_ID + "/load"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(CHESS_GAME_TEST_ID))
                .andExpect(jsonPath("$.color").value("WHITE"))
                .andExpect(jsonPath("$.piecesAndPositions.size()").value(32));
    }

    @Test
    void movePiece() throws Exception {
        Gson gson = new Gson();
        String content = gson.toJson(new MoveRequestDto("a2", "a3"));

        given(chessService.isEnd(CHESS_GAME_TEST_ID)).willReturn(false);
        given(chessService.nextColor(CHESS_GAME_TEST_ID)).willReturn(Color.BLACK);

        mockMvc.perform(MockMvcRequestBuilders
                .post("/games/" + CHESS_GAME_TEST_ID + "/move")
                .content(content).header("Content-Type", "application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.end").value(false))
                .andExpect(jsonPath("$.nextColor").value("BLACK"));
    }
}