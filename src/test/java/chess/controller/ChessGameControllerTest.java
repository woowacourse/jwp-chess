package chess.controller;

import chess.controller.dto.PieceMoveRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ChessGameControllerTest {

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	@DisplayName("새로운 게임 생성")
	void createNewGame() {
		RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("chessgames")
				.then().log().all()
				.statusCode(HttpStatus.CREATED.value())
				.header("Location", "/chessgames/1");
	}

	@Test
	@DisplayName("체스 보드 로딩")
	void loadChessGame() {
		createNewGame();

		RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().get("chessgames/1")
				.then().log().all()
				.statusCode(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("체스 기물 이동")
	void movePiece() {
		createNewGame();

		RestAssured.given().log().all()
				.body(new PieceMoveRequest("a2", "a4"))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("chessgames/1/move")
				.then().log().all()
				.statusCode(HttpStatus.NO_CONTENT.value());
	}
}
