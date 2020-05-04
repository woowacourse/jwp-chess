package wooteco.chess.controller;

import static org.hamcrest.core.StringContains.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import wooteco.chess.entity.Room;
import wooteco.chess.service.ChessService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(GameController.class)
class GameControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ChessService chessService;

    @Test
    void path() throws Exception {
        List<String> path = Arrays.asList("A3", "A4");

        given(chessService.searchPath(new Room("lowoon"), "A2")).willReturn(path);

        mvc.perform(post("/path")
            .contentType(MediaType.APPLICATION_JSON)
            .param("source", "A2")
            .param("roomName", "lowoon"))
            .andExpect(status().isOk())
            .andExpect(content().string("[\"A3\",\"A4\"]"));
    }
}