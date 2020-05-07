package wooteco.chess.controller;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import wooteco.chess.dto.request.MoveRequestDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChessControllerTest {
	@LocalServerPort
	int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	void loadChessGame() {
		given()
			.log().all().
			when()
			.get("/chess/1").
			then()
			.statusCode(200)
			.body(containsString("해당 아이디의 방을 찾을 수 없습니다."));
	}

	@Test
	void newChessGame() {
		given()
			.log().all().
			when()
			.post("/chess/1").
			then()
			.statusCode(200);
	}

	@Test
	void deleteChessGame() {
		given()
			.log().all().
			when()
			.delete("/chess/1").
			then()
			.statusCode(302)
			.header("Location", containsString(String.format("http://localhost:%d/", port)));
	}

	@Test
	void move() {
		MoveRequestDto moveRequestDto = new MoveRequestDto(1L, "a2", "a3");
		given()
			.log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(moveRequestDto)
			.when()
			.post("/chess/1/move")
			.then()
			.statusCode(200)
			.body(containsString("해당 아이디의 방을 찾을 수 없습니다."));

	}
}