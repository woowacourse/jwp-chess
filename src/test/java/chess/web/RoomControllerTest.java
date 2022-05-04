package chess.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import chess.domain.Color;
import chess.domain.board.Board;
import chess.domain.board.RegularRuleSetup;
import chess.domain.chessgame.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.piece.role.Pawn;
import chess.domain.position.Position;
import chess.exception.UserInputException;
import chess.service.ChessGameService;
import chess.web.dto.BoardDto;
import chess.web.dto.RoomResponseDto;

@WebMvcTest({RoomController.class, RoomApiController.class})
class RoomControllerTest {

	private static final String NAME = "summer";
	private static final String PASSWORD = "summer";
	private static final int ROOM_ID = 1;

	@MockBean
	private ChessGameService chessGameService;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("유효한 이름을 받으면 게임방 입장")
	void createRoom() throws Exception {
		given(chessGameService.create(anyString(), anyString()))
			.willReturn(ChessGame.createWithId(
				ROOM_ID, ChessGame.create(NAME, PASSWORD, new Board(new RegularRuleSetup()))));

		mockMvc.perform(post("/rooms")
				.param("name", NAME)
				.param("password", PASSWORD))
			.andExpect(redirectedUrl("/rooms/1"));
	}

	@DisplayName("부적절한 이름이 입력되면 400 에러 발생")
	@ParameterizedTest
	@ValueSource(strings = {"", "16자를넘는방이름은안되니까돌아가"})
	void nameException(String name) throws Exception {
		given(chessGameService.create(anyString(), anyString()))
			.willThrow(UserInputException.class);

		mockMvc.perform(post("/rooms")
				.param("name", name)
				.param("password", PASSWORD))
			.andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("방 목록을 불러온다.")
	void loadRooms() throws Exception {
		List<RoomResponseDto> data = List.of(
			new RoomResponseDto(ROOM_ID, NAME, false),
			new RoomResponseDto(2, "does", false));
		given(chessGameService.findAllResponseDto()).willReturn(data);

		mockMvc.perform(get("/api/rooms"))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(data)));
	}

	@Test
	@DisplayName("새로운 게임을 시작하면 200 응답을 받는다.")
	void startNewGame() throws Exception {
		mockMvc.perform(post("/rooms/" + ROOM_ID))
			.andExpect(status().isFound())
			.andExpect(redirectedUrl("/api/rooms/" + ROOM_ID));
	}

	@Test
	@DisplayName("게임을 불러오면 200 응답을 받는다.")
	void loadGame() throws Exception {
		BoardDto boardDto = new BoardDto(ROOM_ID, new Board(
			() -> Map.of(Position.of("a1"), new Piece(Color.WHITE, new Pawn()))));
		given(chessGameService.getBoardDtoByGameId(ROOM_ID))
			.willReturn(boardDto);

		mockMvc.perform(get("/api/rooms/" + ROOM_ID))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(boardDto)));
	}

	@Test
	@DisplayName("방을 삭제한다.")
	void deleteRoom() throws Exception {
		mockMvc.perform(delete("/api/rooms/" + ROOM_ID)
				.param("password", PASSWORD))
			.andExpect(status().isOk());
	}

	@Test
	@DisplayName("방 삭제에 실패하면 400 에러가 발생한다.")
	void deleteRoomException() throws Exception {
		doThrow(UserInputException.class)
			.when(chessGameService).delete(ROOM_ID, PASSWORD);

		mockMvc.perform(delete("/api/rooms/" + ROOM_ID)
				.param("password", PASSWORD))
			.andExpect(status().isBadRequest());
	}
}
