package chess.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import chess.domain.Winner;
import chess.domain.board.Board;
import chess.domain.board.BoardInitializer;
import chess.domain.board.Position;
import chess.dto.BoardInfoDto;
import chess.dto.ChessBoardDto;
import chess.dto.MovePositionDto;
import chess.dto.ResultDto;
import chess.dto.StatusDto;
import chess.service.ChessGameService;
import com.google.gson.Gson;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChessSpringController.class)
public class ChessSpringControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChessGameService chessGameService;

    @DisplayName("rootPage GET 요청 테스트")
    @Test
    void get_start() throws Exception {
        List<BoardInfoDto> boardInfo = List.of(new BoardInfoDto(1, "name"));
        given(chessGameService.getBoards()).willReturn(boardInfo);

        mockMvc.perform(get("/"))
                .andExpect(model().attribute("boards", boardInfo))
                .andExpect(view().name("home"));
    }

    @DisplayName("chess GET 요청 테스트")
    @Test
    void get_chess() throws Exception {
        final ChessBoardDto chessBoardDto = ChessBoardDto.from(new Board().getPiecesByPosition());
        given(chessGameService.getPieces(1)).willReturn(BoardInitializer.createBoard());

        mockMvc.perform(get("/board?id=1"))
                .andExpect(view().name("index"))
                .andExpect(model().attribute("boardDto", chessBoardDto));
    }

    @DisplayName("move POST 요청 테스트")
    @Test
    void post_move() throws Exception {
        final Gson gson = new Gson();
        given(chessGameService.move(Position.from("a2"), Position.from("a4"), 1)).willReturn(HttpStatus.OK);

        mockMvc.perform(put("/board?id=1")
                        .header("Content-Type", "application/json")
                        .content(gson.toJson(new MovePositionDto("a2", "a4"))))
                .andExpect(status().isOk());
    }

    @DisplayName("status GET 요청 테스트")
    @Test
    void get_status() throws Exception {
        final StatusDto statusDto = StatusDto.of(38, 38);
        given(chessGameService.status(1)).willReturn(statusDto);

        mockMvc.perform(get("/board/status?id=1"))
                .andExpect(view().name("boardStatus"))
                .andExpect(model().attribute("status", statusDto));
    }

    @DisplayName("end GET 요청 테스트")
    @Test
    void get_end() throws Exception {
        mockMvc.perform(get("/board/end?id=1"))
                .andExpect(status().isOk());
    }

    @DisplayName("result GET 요청 테스트")
    @Test
    void get_result() throws Exception {
        final ResultDto resultDto = ResultDto.of(38, 38, Winner.DRAW);
        given(chessGameService.result(1)).willReturn(resultDto);

        mockMvc.perform(get("/board/result?id=1"))
                .andExpect(view().name("boardResult"))
                .andExpect(model().attribute("result", resultDto));
    }
}
