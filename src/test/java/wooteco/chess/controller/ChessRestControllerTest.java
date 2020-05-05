package wooteco.chess.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;

@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChessRestControllerTest {
	@LocalServerPort
	int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	void getRooms() {
		given().
				log().all().
				when().
				get("/rooms").
				then().
				log().all().
				body(containsString("\"statusCode\":1,\"dto\":")).
				statusCode(200);
	}

	// @TODO regexp로 방 번호 확인
	@Test
	void createRoom() {
		given().
				log().all().
				when().
				post("/rooms").
				then().
				log().all().
				body(containsString("\"statusCode\":1,\"dto\":")).
				statusCode(200);
	}
}