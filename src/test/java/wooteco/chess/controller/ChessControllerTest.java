package wooteco.chess.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import wooteco.chess.dao.GameDao;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.dto.MoveResponseDto;
import wooteco.chess.service.ChessService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChessControllerTest {
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Autowired
	private GameDao gameDao;

	@Autowired
	private ChessService chessService;

	@LocalServerPort
	private int port;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.addFilter(new CharacterEncodingFilter("UTF-8", true))
			.build();
		RestAssured.port = port;
	}

	@Test
	void index() {
		given().log().all()
			.when().get("/")
			.then().log().all();
	}

	@Test
	void start() {
		given().log().all()
			.when().post("/start")
			.then().log().all()
			.statusCode(200);
	}

	@Test
	void move() throws Exception {
		//given
		BoardDto game = chessService.createGame();
		MoveRequestDto moveRequestDto = new MoveRequestDto(game.getGameId(), "move a2 a3");

		//when
		MvcResult mvcResult = mockMvc.perform(post("http://localhost:" + port + "/move")
			.contentType(MediaType.APPLICATION_JSON)
			.content(gson.toJson(moveRequestDto)))
			.andDo(print())
			.andExpect(status().is(200))
			.andReturn();

		String contentAsString = mvcResult.getResponse().getContentAsString();
		ObjectMapper objectMapper = new ObjectMapper();
		MoveResponseDto moveResponseDto = objectMapper.readValue(contentAsString, MoveResponseDto.class);

		//then
		assertThat(moveResponseDto.getGameId()).isEqualTo(game.getGameId());
		assertThat(moveResponseDto.getPiece()).isEqualTo("♙");
		assertThat(moveResponseDto.getTurn()).isEqualTo("black");
	}
}