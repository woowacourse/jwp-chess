package chess.controller;

import chess.domain.ChessGame;
import chess.domain.team.Team;
import chess.service.SpringChessService;
import chess.webdto.view.ChessGameDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SpringChessController.class)
class SpringChessControllerTest {
    @MockBean
    private SpringChessService springChessService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void startNewGame() throws  Exception{
        when(springChessService.startNewGame())
                .thenReturn(new ChessGameDto(new ChessGame(Team.blackTeam(), Team.whiteTeam())));

        mockMvc.perform(post("/game"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("currentTurnTeam").value("white"))
                .andExpect(jsonPath("piecePositionByTeam.white.*", hasSize(16)))
                .andExpect(jsonPath("piecePositionByTeam.black.*", hasSize(16)))
                .andExpect(jsonPath("isPlaying").value(true))
                .andExpect(jsonPath("teamScore.black").value(38.0));
    }
}