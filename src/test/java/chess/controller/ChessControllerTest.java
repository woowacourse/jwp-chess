package chess.controller;

import chess.dto.ChessGameStatusDto;
import chess.service.ChessGameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@WebMvcTest
class ChessControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChessGameService chessGameService;

    @DisplayName("최근 실행중인 게임이 존재할 때, index page 요청을 테스트한다")
    @Test
    void testIndexIfExistPlayingGame() throws Exception {
        //given
        ChessGameStatusDto existChessGameStatus = ChessGameStatusDto.exist();
        when(chessGameService.findLatestChessGameStatus())
                .thenReturn(existChessGameStatus);

        //when //then
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("status"))
                .andExpect(model().attribute("status",existChessGameStatus))
                .andExpect(view().name("index"));
    }

    @DisplayName("최근 실행중인 게임이 존재하지 않을 때, index page 요청을 테스트한다")
    @Test
    void testIndexIfNotExistPlayingGame() throws Exception {
        //given
        ChessGameStatusDto notExistChessGameStatus = ChessGameStatusDto.isNotExist();
        when(chessGameService.findLatestChessGameStatus())
                .thenReturn(notExistChessGameStatus);

        //when //then
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("status"))
                .andExpect(model().attribute("status", notExistChessGameStatus))
                .andExpect(view().name("index"));
    }

}
