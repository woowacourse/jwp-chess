package chess.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import chess.config.HandlebarConfig;
import chess.domain.Score;
import chess.domain.piece.Color;
import chess.dto.ChessGameDto;
import chess.dto.GameStatus;
import chess.service.ChessGameService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(LobbyController.class)
@ContextConfiguration(classes = HandlebarConfig.class)
public class LobbyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChessGameService chessGameService;

    @Test
    void lobby() throws Exception {
        List<ChessGameDto> dtos = new ArrayList<>();
        dtos.add(new ChessGameDto(1, "hoho", GameStatus.RUNNING, new Score(), new Score(), Color.BLACK));

        Mockito.when(chessGameService.findAll())
            .thenReturn(dtos);

        mockMvc.perform(get("/"))
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("index"),
                        model().attributeExists("chess-games")
                );
    }

}
