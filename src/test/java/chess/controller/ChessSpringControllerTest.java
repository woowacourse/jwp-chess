package chess.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import chess.domain.board.Board;
import chess.dto.ChessBoardDto;
import chess.dto.ResponseDto;
import chess.service.ChessGameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChessSpringController.class)
public class ChessSpringControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChessGameService chessGameService;

    @DisplayName("start Test- GET")
    @Test
    void get_start() throws Exception {
        ResponseDto responseDto = new ResponseDto(200, "");
        given(chessGameService.start()).willReturn(new ResponseDto(200, ""));

        mockMvc.perform(get("/start"))
                .andExpect(content().json(responseDto.toJson()));
    }

    @DisplayName("chess Test- GET")
    @Test
    void get_chess() throws Exception {
        final ChessBoardDto chessBoardDto = ChessBoardDto.from(new Board().getPiecesByPosition());
        given(chessGameService.getBoard()).willReturn(ChessBoardDto.from(new Board().getPiecesByPosition()));

        mockMvc.perform(get("/chess"))
                .andExpect(view().name("index"))
                .andExpect(model().attribute("boardDto", chessBoardDto));
    }
}
