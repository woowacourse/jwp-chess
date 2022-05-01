package chess.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import chess.domain.Winner;
import chess.dto.ResultDto;
import chess.dto.RoomDto;
import chess.dto.RoomsDto;
import chess.dto.StatusDto;
import chess.service.ChessGameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(ChessSpringController.class)
public class ChessSpringControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ChessGameService chessGameService;

    @Test
    @DisplayName("GET / : view는 home, model attribute는 roomsDto 반환")
    void get_home() throws Exception {
        final RoomsDto roomsDto = new RoomsDto(List.of(new RoomDto(1L, "abc")));

        given(chessGameService.getRooms()).willReturn(roomsDto);

        mockMvc.perform(get("/"))
                .andExpect(view().name("home"))
                .andExpect(model().attribute("roomsDto", roomsDto));
    }

    @Test
    @DisplayName("POST / : 20자 이하의 방제목, 비밀번호를 body로 받으면 201 상태코드 반환")
    void post_create() throws Exception {
        final String title = "abc";
        final String password = "123";
        final Map<String, String> body = new HashMap<>();
        body.put("title", title);
        body.put("password", password);

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST / : 20자 초과의 방제목, 비밀번호를 body로 받으면 400 상태코드 반환")
    void post_invalid_create() throws Exception {
        final String title = "abcdeabcdeabcdeabcdea";
        final String password = "123";
        final Map<String, String> body = new HashMap<>();
        body.put("title", title);
        body.put("password", password);

        mockMvc.perform(post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("PATCH /board/move : 200 상태코드 반환")
    void patch_move() throws Exception {
        final String requestString = "a2 a4";

        mockMvc.perform(patch("/board/move")
                .queryParam("id", "1")
                .content(requestString))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /board/chess-status : view는 status, model attribute는 statusDto를 반환")
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
    @DisplayName("GET /board/chess-result : view는 result, model attribute는 resultDto를 반환")
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
    @DisplayName("POST /board/restart : 200 상태코드 반환")
    void post_restart() throws Exception {
        mockMvc.perform(post("/board/restart")
                .queryParam("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /board/end : 200 상태코드 반환")
    void post_end() throws Exception {
        mockMvc.perform(post("/board/end")
                .queryParam("id", "1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /board : 200 상태코드 반환")
    void delete() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/board")
                .queryParam("id", "1")
                .content("123"))
                .andExpect(status().isOk());
    }
}
