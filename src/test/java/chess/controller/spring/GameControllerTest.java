package chess.controller.spring;

import chess.domain.ChessGameManager;
import chess.domain.board.ChessBoard;
import chess.domain.piece.Color;
import chess.domain.piece.King;
import chess.domain.piece.Piece;
import chess.domain.piece.Rook;
import chess.domain.position.Position;
import chess.dto.CommonDto;
import chess.dto.NewGameDto;
import chess.dto.RunningGameDto;
import chess.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;

import static chess.domain.piece.Color.BLACK;
import static chess.domain.piece.Color.WHITE;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GameController.class)
class GameControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    GameService gameService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void 새_게임_생성() throws Exception {
        // given
        ChessGameManager chessGameManager = new ChessGameManager();
        chessGameManager.start();
        NewGameDto newGameDto = NewGameDto.from(chessGameManager, 1);
        final CommonDto<NewGameDto> commonDto = new CommonDto<>("새로운 게임이 생성되었습니다.", newGameDto);
        given(gameService.saveNewGame()).willReturn(commonDto);

        // when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
                .get("/game/new")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(commonDto)));

        // then
        actions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("message").value("새로운 게임이 생성되었습니다."))
                .andExpect(jsonPath("item.gameId").value(1))
                .andExpect(jsonPath("item.chessBoard.size()").value(32));
    }

    @Test
    void 게임_불러오기() throws Exception {
        // given
        ChessGameManager chessGameManager = new ChessGameManager();
        HashMap<Position, Piece> board = new HashMap<Position, Piece>() {{
            put(Position.of("a1"), new Rook(WHITE));
            put(Position.of("e1"), new King(WHITE));
            put(Position.of("e8"), (new King(BLACK)));
        }};
        ChessBoard chessBoard = ChessBoard.from(board);
        chessGameManager.load(chessBoard, Color.WHITE);

        RunningGameDto runningGameDto = RunningGameDto.of(
                chessBoard,
                chessGameManager.getCurrentTurnColor(),
                chessGameManager.isEnd());
        final CommonDto<RunningGameDto> commonDto = new CommonDto<>("게임을 불러왔습니다.", runningGameDto);

        given(gameService.loadGame(2)).willReturn(commonDto);

        // when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders
                .get("/game/2/load")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(commonDto)));

        // then
        actions.andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("message").value("게임을 불러왔습니다."))
                .andExpect(jsonPath("item.colorsScore.WHITE").value(5.0))
                .andExpect(jsonPath("item.colorsScore.BLACK").value(0.0))
                .andExpect(jsonPath("item.currentTurnColor").value("WHITE"))
                .andExpect(jsonPath("item.end").value(false))
                .andExpect(jsonPath("item.chessBoard.a1.name").value("Rook"))
                .andExpect(jsonPath("item.chessBoard.a1.color").value("WHITE"))
                .andExpect(jsonPath("item.chessBoard.e1.name").value("King"))
                .andExpect(jsonPath("item.chessBoard.e1.color").value("WHITE"))
                .andExpect(jsonPath("item.chessBoard.e8.name").value("King"))
                .andExpect(jsonPath("item.chessBoard.e8.color").value("BLACK"));
    }
}