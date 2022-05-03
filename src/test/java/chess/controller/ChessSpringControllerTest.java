package chess.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import chess.domain.Winner;
import chess.dto.ResultDto;
import chess.dto.RoomDto;
import chess.dto.RoomsDto;
import chess.dto.StatusDto;
import chess.service.ChessGameService;
import java.util.List;
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
    @DisplayName("GET /boards/{id}/chess-status : view는 status, model attribute는 statusDto를 반환")
    void get_status() throws Exception {
        final StatusDto statusDto = StatusDto.of(37, 37);
        given(chessGameService.statusOfWhite(1L)).willReturn(37.0);
        given(chessGameService.statusOfBlack(1L)).willReturn(37.0);

        mockMvc.perform(get("/boards/1/chess-status"))
                .andExpect(view().name("status"))
                .andExpect(model().attribute("status", statusDto));
    }

    @Test
    @DisplayName("GET /boards/{id}/chess-result : view는 result, model attribute는 resultDto를 반환")
    void get_result() throws Exception {
        final ResultDto resultDto = ResultDto.of(37, 36, Winner.WHITE);
        given(chessGameService.statusOfWhite(1L)).willReturn(37.0);
        given(chessGameService.statusOfBlack(1L)).willReturn(36.0);
        given(chessGameService.findWinner(1L)).willReturn(Winner.WHITE);

        mockMvc.perform(get("/boards/1/chess-result"))
                .andExpect(view().name("result"))
                .andExpect(model().attribute("result", resultDto));

    }
}
