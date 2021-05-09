package chess.controller;

import chess.controller.dto.GameDto;
import chess.domain.game.MoveRequest;
import chess.exception.ErrorCode;
import chess.service.ChessService;
import chess.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest
@AutoConfigureMockMvc
class ChessApiControllerTest {
    @Autowired
    ChessService chessService;
    @Autowired
    GameService gameService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private final GameDto gameDto = new GameDto("test-game");

    @BeforeEach
    void setUp() {
        gameService.create(new GameDto("before-each1"));
        gameService.create(new GameDto("before-each2"));
        gameService.create(new GameDto("before-each3"));
    }

    @Test
    @DisplayName("게임 리스트 불러오기")
    void loadGameList() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/games");

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(gameService.allGame())))
               .andDo(print());
    }

    @Test
    @DisplayName("새 게임 생성 성공")
    void createGame() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/games")
                                                                             .content(objectMapper.writeValueAsBytes(gameDto))
                                                                             .contentType(MediaType.APPLICATION_JSON);

        int expectedSize = gameService.allGame()
                                      .size() + 1;

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(content().string(Integer.toString(expectedSize)))
               .andDo(print());
    }

    @Test
    @DisplayName("게임 로드 성공")
    void loadGame() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/games/1/load");

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(chessService.load(1L))))
               .andDo(print());
    }

    @Test
    @DisplayName("게임 로드 실패")
    void loadNonExistGame() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/games/10/load");

        mockMvc.perform(requestBuilder)
               .andExpect(status().isBadRequest())
               .andExpect(content().string(ErrorCode.NO_ROOM_TO_LOAD.getMessage()))
               .andDo(print());
    }

    @Test
    @DisplayName("게임 삭제 성공")
    void deleteGame() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/games/1");

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk());

        assertThat(gameService.allGame()).hasSize(2);
    }

    @Test
    @DisplayName("게임 삭제 실패")
    void deleteNonExistGame() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.delete("/games/10");

        mockMvc.perform(requestBuilder)
               .andExpect(status().isBadRequest())
               .andExpect(content().string(ErrorCode.NO_ROOM_TO_DELETE.getMessage()))
               .andDo(print());
    }

    @Test
    @DisplayName("게임 이동 성공")
    void move() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/games/1/move")
                                                                             .contentType(MediaType.APPLICATION_JSON)
                                                                             .content(objectMapper.writeValueAsString(new MoveRequest("c7", "c5")));

        mockMvc.perform(requestBuilder)
               .andExpect(status().isOk())
               .andExpect(content().json(objectMapper.writeValueAsString(chessService.load(1L))));
    }


    @Test
    @DisplayName("게임 이동 실패")
    void wrongMove() throws Exception {
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/games/1/move")
                                                                             .contentType(MediaType.APPLICATION_JSON)
                                                                             .content(objectMapper.writeValueAsString(new MoveRequest("c2", "c5")));

        mockMvc.perform(requestBuilder)
               .andExpect(status().isBadRequest())
               .andExpect(content().string(ErrorCode.INVALID_PIECE_SELECT.getMessage()))
               .andDo(print());
    }
}