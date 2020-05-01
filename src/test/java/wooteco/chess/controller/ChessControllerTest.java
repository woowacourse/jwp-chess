package wooteco.chess.controller;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Objects;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
import wooteco.chess.domain.board.ChessGame;
import wooteco.chess.domain.entity.Game;
import wooteco.chess.dto.BoardDto;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.dto.MoveResponseDto;
import wooteco.chess.dto.RoomsDto;
import wooteco.chess.repository.GameRepository;
import wooteco.chess.service.ChessService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChessControllerTest {
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	@Autowired
	private GameRepository gameRepository;

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

	@AfterEach
	void tearDown() {
		gameRepository.deleteAll();
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

	@Test
	void save() {
		given().log().all()
			.when().get("/save")
			.then().log().all()
			.statusCode(200)
			.body(containsString("저장되었습니다."));
	}

	@Test
	void load() throws Exception {
		//given
		BoardDto game = chessService.createGame();

		//when
		MvcResult mvcResult = mockMvc.perform(get("http://localhost:" + port + "/load?gameId=" + game.getGameId()))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		//then
		BoardDto response = (BoardDto)Objects.requireNonNull(mvcResult.getModelAndView()).getModel().get("response");
		assertThat(response.getGameId()).isEqualTo(game.getGameId());
	}

	@DisplayName("게임 목록 불러오기")
	@Test
	void loadGameList() throws Exception {
		//given
		Game game1 = new Game(new ChessGame());
		Game game2 = new Game(new ChessGame());

		gameRepository.save(game1);
		gameRepository.save(game2);

		//when
		MvcResult mvcResult = mockMvc.perform(get("http://localhost:8080"))
			.andDo(print())
			.andExpect(status().isOk())
			.andReturn();

		RoomsDto roomsDto = (RoomsDto)mvcResult.getModelAndView().getModel().get("room");

		//then
		assertThat(roomsDto.getRoom()).hasSize(2);
	}
}