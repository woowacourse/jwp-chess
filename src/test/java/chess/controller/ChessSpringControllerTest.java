package chess.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import chess.domain.Winner;
import chess.dto.ResponseDto;
import chess.dto.ResultDto;
import chess.dto.StatusDto;
import chess.service.ChessGameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChessSpringController.class)
public class ChessSpringControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChessGameService chessGameService;

    @Test
    @DisplayName("POST /create 테스트")
    void post_create() throws Exception {
        final ResponseDto responseDto = new ResponseDto(200, "");
        final String title = "abc";
        final String password = "123";
        final Map<String, String> body = new HashMap<>();
        body.put("title", title);
        body.put("password", password);
        given(chessGameService.create(title, password)).willReturn(new ResponseDto(200, ""));

        mockMvc.perform(post("/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(content().json(responseDto.toJson()));
    }

    @Test
    @DisplayName("POST /board/move 테스트")
    void post_move() throws Exception {
        final ResponseDto responseDto = new ResponseDto(302, "");
        final Long boardId = 1L;
        final String requestString = "a2 a4";

        given(chessGameService.move(boardId, "a2", "a4")).willReturn(responseDto);

        mockMvc.perform(post("/board/move")
                .queryParam("id", "1")
                .content(requestString))
                .andExpect(content().json(responseDto.toJson()));
    }

    @Test
    @DisplayName("GET /board/chess-status 테스트")
    void get_status() throws Exception {
        final StatusDto statusDto = StatusDto.of(37, 37);
        given(chessGameService.statusOfWhite(1L)).willReturn(37.0);
        given(chessGameService.statusOfBlack(1L)).willReturn(37.0);

        mockMvc.perform(get("/board/chess-status")
                .queryParam("id", "1"))
                .andExpect(view().name("status"))
                .andExpect(model().attribute("status", statusDto));
    }

    @Test
    @DisplayName("GET /board/chess-result 테스트")
    void get_result() throws Exception {
        final ResultDto resultDto = ResultDto.of(37, 36, Winner.WHITE);
        given(chessGameService.statusOfWhite(1L)).willReturn(37.0);
        given(chessGameService.statusOfBlack(1L)).willReturn(36.0);
        given(chessGameService.findWinner(1L)).willReturn(Winner.WHITE);

        mockMvc.perform(get("/board/chess-result")
                .queryParam("id", "1"))
                .andExpect(view().name("result"))
                .andExpect(model().attribute("result", resultDto));

    }

    @Test
    @DisplayName("POST /board/end 테스트")
    void post_end() throws Exception {
        final ResponseDto responseDto = new ResponseDto(201, "");
        given(chessGameService.end(1L)).willReturn(new ResponseDto(201, ""));

        mockMvc.perform(post("/board/end")
                .queryParam("id", "1"))
                .andExpect(content().json(responseDto.toJson()));
    }
}
