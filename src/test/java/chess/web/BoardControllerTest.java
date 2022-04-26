package chess.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import chess.configuration.RepositoryConfiguration;
import chess.service.GameService;
import chess.service.RoomService;
import chess.web.dto.CommendDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(RepositoryConfiguration.class)
class BoardControllerTest {

	@LocalServerPort
	private int port;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private RoomService roomService;
	@Autowired
	private GameService gameService;
	private int boardId;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		int roomId = (int) roomService.create("summer").getId();
		boardId = gameService.startNewGame(roomId).getBoardId();
	}

	@Test
	@DisplayName("체스 말을 움직이면 200응답을 받는다.")
	void move() {
		RestAssured.given().log().all()
			.contentType(ContentType.JSON)
			.body(new CommendDto("a2", "a3"))
			.when().post("/boards/" + boardId + "/move")
			.then().log().all()
			.statusCode(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("점수 갱신 요청이 누르면 200응답을 받는다.")
	void result() {
		RestAssured.given().log().all()
			.when().get("/boards/" + boardId + "/result")
			.then().log().all()
			.statusCode(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("게임 종료 요청이 오면 200응답을 받는다.")
	void end() {
		RestAssured.given().log().all()
			.when().get("/boards/" + boardId + "/end")
			.then().log().all()
			.statusCode(HttpStatus.OK.value());
	}
}
