package chess.controller;

import chess.domain.board.Board;
import chess.domain.game.ChessGame;
import chess.domain.game.ChessGameEntity;
import chess.domain.piece.PieceFactory;
import chess.dto.ChessGameInfoResponseDto;
import chess.dto.PlayingChessgameEntityDto;
import chess.exception.NotFoundChessGamePageException;
import chess.service.ChessGameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

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

    @DisplayName("체스방 목록을 조회할 수 있는 index page 요청을 테스트한다")
    @Test
    void testIndex() throws Exception {
        //given
        ChessGameEntity firstChessGameEntity = new ChessGameEntity(1L, "BlackTurn", "title1");
        ChessGameEntity secondChessGameEntity = new ChessGameEntity(2L, "Ready", "title2");
        List<PlayingChessgameEntityDto> playingChessgameEntityDtos = Arrays.asList(
                new PlayingChessgameEntityDto(firstChessGameEntity),
                new PlayingChessgameEntityDto(secondChessGameEntity)
        );
        when(chessGameService.findAllPlayingGames()).thenReturn(playingChessgameEntityDtos);

        //when //then
        this.mockMvc.perform(get("/"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("chessGames"))
                .andExpect(model().attribute("chessGames", playingChessgameEntityDtos))
                .andExpect(view().name("index"));
    }

    @DisplayName("특정 체스게임 페이지로 이동한다")
    @Test
    void testChessGameIfExistPlayingGame() throws Exception {
        //given
        ChessGame chessGame = new ChessGame(new Board(PieceFactory.createPieces()));
        ChessGameInfoResponseDto expectedChessGameInfoResponseDto = new ChessGameInfoResponseDto(1L, chessGame, "title");
        when(chessGameService.findChessGameInfoById(1L))
                .thenReturn(expectedChessGameInfoResponseDto);

        //when //then
        this.mockMvc.perform(get("/chessgames/" + 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("chessGameInfo"))
                .andExpect(model().attribute("chessGameInfo", expectedChessGameInfoResponseDto))
                .andExpect(view().name("chessgame-room"));
    }

    @DisplayName("존재하지 않는 체스게임 페이지로 이동한다")
    @Test
    void testChessGameIfNotExistPlayingGame() throws Exception {
        //given
        when(chessGameService.findChessGameInfoById(1L))
                .thenThrow(new NotFoundChessGamePageException());

        //when //then
        this.mockMvc.perform(get("/chessgames/" + 1L))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(model().attribute("errorMessage", "해당 체스게임 페이지를 찾을 수 없습니다."))
                .andExpect(view().name("error-page"));
    }

}
