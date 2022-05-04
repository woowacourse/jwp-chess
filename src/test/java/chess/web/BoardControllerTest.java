package chess.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import chess.domain.Color;
import chess.domain.Result;
import chess.domain.board.Board;
import chess.domain.piece.Piece;
import chess.domain.piece.role.Pawn;
import chess.domain.position.Position;
import chess.service.ChessGameService;
import chess.web.dto.BoardDto;
import chess.web.dto.CommendDto;
import chess.web.dto.ResultDto;

@WebMvcTest(BoardController.class)
public class BoardControllerTest {

	@MockBean
	private ChessGameService chessGameService;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("체스 말을 움직이면 200응답을 받는다.")
	void move() throws Exception {
		String command = objectMapper.writeValueAsString(
			new CommendDto("a2", "a3"));
		BoardDto boardDto = new BoardDto(1, new Board(
			() -> Map.of(Position.of("a1"), new Piece(Color.WHITE, new Pawn()))));
		String board = objectMapper.writeValueAsString(boardDto);

		given(chessGameService.getBoardDtoByBoardId(1))
			.willReturn(boardDto);

		mockMvc.perform(put("/boards/" + 1)
				.content(command)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().json(board));
	}

	@Test
	@DisplayName("점수 갱신 요청이 누르면 200응답을 받는다.")
	void result() throws Exception {
		ResultDto resultDto = new ResultDto(38, 37, Map.of(Result.WIN, Color.WHITE));
		given(chessGameService.getResult(1))
			.willReturn(resultDto);

		mockMvc.perform(get("/boards/" + 1 + "/result"))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(resultDto)));
	}

	@Test
	@DisplayName("게임 종료 요청이 오면 200응답을 받는다.")
	void end() throws Exception {
		ResultDto resultDto = new ResultDto(38, 37, Map.of(Result.WIN, Color.WHITE));
		given(chessGameService.getFinalResult(1))
			.willReturn(resultDto);

		mockMvc.perform(put("/boards/" + 1 + "/end"))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(resultDto)));
	}
}
