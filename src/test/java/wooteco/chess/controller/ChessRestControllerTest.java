package wooteco.chess.controller;

import static io.restassured.RestAssured.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;

@AutoConfigureTestDatabase
@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChessRestControllerTest {
	@LocalServerPort
	int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@Test
	@DisplayName("체스방 생성")
	void createRoom() {
		when()
				.post("/rooms")
		.then()
				.log().all()
				.statusCode(200);
	}

	@Test
	@DisplayName("체스방 목록")
	void getRooms() {
		when()
				.get("/rooms")
		.then().
				log().all()
				.statusCode(200);
	}

	@Test
	@DisplayName("체스방 삭제")
	void deleteRoom() {
		when()
				.delete("/rooms/1")
		.then()
				.log().all()
				.statusCode(200);
	}
}