package chess.controller.api;

import chess.controller.ExceptionController;
import chess.domain.board.ChessBoard;
import chess.domain.board.Position;
import chess.domain.game.ChessGame;
import chess.domain.piece.Color;
import chess.dto.ChessGameDto;
import chess.dto.ChessGamesDto;
import chess.dto.MoveDto;
import chess.dto.TitleDto;
import chess.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {GameController.class, ExceptionController.class})
class GameControllerTest {

    @MockBean
    private GameService gameService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("id로 게임 불러오기")
    void loadGame() throws Exception {
        ChessGame chessGame = new ChessGame(1L, Color.WHITE, false, new ChessBoard(), "test-game");
        ChessGameDto chessGameDto = new ChessGameDto(chessGame);
        when(gameService.loadGame(1L)).thenReturn(chessGame);

        mockMvc.perform(get("/game/1/game-info"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(chessGameDto)));

        verify(gameService, times(1)).loadGame(1L);
        verifyNoMoreInteractions(gameService);
    }

    @Test
    @DisplayName("움직일 수 있는 경우 200")
    void moveSuccess() throws Exception {
        MoveDto moveDto = new MoveDto("a2", "a3");
        ChessGame chessGame = new ChessGame(1L, Color.WHITE, false, new ChessBoard(), "test-game");
        chessGame.move(Position.of(moveDto.getSource()), Position.of(moveDto.getTarget()));
        when(gameService.loadGame(1L))
                .thenReturn(chessGame);
        doNothing().when(gameService).move(1L, moveDto);

        mockMvc.perform(put("/game/1/move")
                .content(objectMapper.writeValueAsString(moveDto))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(new ChessGameDto(chessGame))));

        verify(gameService, times(1)).move(any(), any());
        verify(gameService, times(1)).loadGame(1L);
        verifyNoMoreInteractions(gameService);
    }

    @Test
    @DisplayName("움직일 수 없는 경우 404")
    void moveFailed() throws Exception {
        doThrow(new IllegalArgumentException()).when(gameService).move(eq(1L), any());

        mockMvc.perform(put("/game/1/move")
                .content(objectMapper.writeValueAsString(new MoveDto("b1", "b2")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(gameService, times(1)).move(any(), any());
        verifyNoMoreInteractions(gameService);
    }

    @Test
    @DisplayName("게임 종료")
    void terminateGame() throws Exception {
        doNothing().when(gameService).terminateGame(1L);

        mockMvc.perform(post("/game/1/terminate"))
                .andExpect(status().isOk());

        verify(gameService, times(1)).terminateGame(1L);
        verifyNoMoreInteractions(gameService);
    }

    @Test
    @DisplayName("게임 재시작")
    void restart() throws Exception {
        ChessGame chessGame = new ChessGame(1L, Color.WHITE, false, new ChessBoard(), "test-game");
        ChessGameDto chessGameDto = new ChessGameDto(chessGame);
        when(gameService.restart(1L)).thenReturn(chessGame);

        mockMvc.perform(post("/game/1/restart"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(chessGameDto)));

        verify(gameService, times(1)).restart(1L);
        verifyNoMoreInteractions(gameService);
    }

    @Test
    @DisplayName("게임 생성 성공")
    void newGameSuccess() throws Exception {
        TitleDto newGameTitle = new TitleDto("test-room");
        when(gameService.newGame(newGameTitle.getTitle())).thenReturn(1L);
        doNothing().when(gameService).verifyDuplicateTitleInGames(newGameTitle.getTitle());

        mockMvc.perform(post("/game")
                .content(objectMapper.writeValueAsString(newGameTitle))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));

        verify(gameService, times(1)).newGame(newGameTitle.getTitle());
        verify(gameService, times(1)).verifyDuplicateTitleInGames(newGameTitle.getTitle());
        verifyNoMoreInteractions(gameService);
    }

    @Test
    @DisplayName("게임 생성 실패")
    void newGameFailed() throws Exception {
        TitleDto newGameTitle = new TitleDto("test-room");
        when(gameService.newGame(newGameTitle.getTitle())).thenReturn(1L);
        doThrow(new IllegalArgumentException()).when(gameService).verifyDuplicateTitleInGames(newGameTitle.getTitle());

        mockMvc.perform(post("/game")
                .content(objectMapper.writeValueAsString(newGameTitle))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verify(gameService, times(1)).verifyDuplicateTitleInGames(newGameTitle.getTitle());
        verifyNoMoreInteractions(gameService);
    }

    @Test
    @DisplayName("모든 방 찾기")
    void findAllRooms() throws Exception {
        List<ChessGame> games = Arrays.asList(
                new ChessGame(),
                new ChessGame(),
                new ChessGame()
        );
        when(gameService.findAllGames()).thenReturn(games);
        ChessGamesDto chessGamesDto = new ChessGamesDto(
                gameService.findAllGames()
                        .stream()
                        .map(ChessGameDto::new)
                        .collect(Collectors.toList())
        );

        mockMvc.perform(get("/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("gameList", hasSize(3)))
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(chessGamesDto)));

        verify(gameService, times(2)).findAllGames();
        verifyNoMoreInteractions(gameService);
    }
}