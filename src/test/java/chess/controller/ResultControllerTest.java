package chess.controller;

import static org.hamcrest.core.StringContains.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import chess.domain.game.GameResult;
import chess.domain.piece.Color;
import chess.service.ChessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ResultController.class)
public class ResultControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean(ChessService.class)
    ChessService chessService;

    private GameResult gameResult;

    @Test
    public void returnStatus() throws Exception {
        given(gameResult.calculateScore(any(Color.class))).willReturn(0D);

        mvc.perform(get("/status")
                        .param("gameCode", "test"))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl("/status"));
    }
}
