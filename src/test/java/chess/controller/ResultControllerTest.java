package chess.controller;

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

import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ResultController.class)
public class ResultControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean(ChessService.class)
    ChessService chessService;

    private final GameResult gameResult = mock(GameResult.class);

    @Test
    public void returnStatus() throws Exception {
        given(gameResult.calculateScore(any(Color.class))).willReturn(0D);
        given(chessService.findGameID(anyString())).willReturn("test");
        given(chessService.getGameResult(anyString())).willReturn(gameResult);

        mvc.perform(get("/status")
                        .param("gameCode", "test"))
                .andExpect(status().isOk())
                .andExpect(view().name("final"));
    }
}
