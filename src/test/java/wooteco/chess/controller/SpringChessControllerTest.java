package wooteco.chess.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import wooteco.chess.service.ChessService;
import wooteco.chess.service.dto.ChessGameDto;

@RunWith(SpringRunner.class)
@WebMvcTest(SpringChessController.class)
class SpringChessControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ChessService chessService;

	@MockBean
	private ChessGameDto chessGameDto;

	@DisplayName("index페이지로 간다.")
	@Test
	void goToIndexPage() throws Exception {
		mockMvc.perform(get("/"))
			.andExpect(status().isOk())
			.andExpect(view().name("index"));
	}
}