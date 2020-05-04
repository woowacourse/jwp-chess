package wooteco.chess.controller;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import wooteco.chess.service.ChessGameService;

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

	@Autowired
	ChessGameService chessGameService;

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
		Long id = chessGameService.create();
		when()
				.delete("/rooms/" + id)
		.then()
				.log().all()
				.statusCode(200);
	}

	@Test
	@DisplayName("체스 재시작")
	void restartRoom() {
		Long id = chessGameService.create();
		when()
				.put("/rooms/" + id)
		.then()
				.log().all()
				.statusCode(200);
	}

	@Test
	@DisplayName("체스 보드 가져오기")
	void getBoard() {
		Long id = chessGameService.create();
		when()
				.get("/rooms/" + id + "/board")
		.then()
				.log().all()
				.statusCode(200);
	}

	@Test
	@DisplayName("체스 말 이동")
	void movePiece() {
		Map<String, Object> requestData = new HashMap<>();
		requestData.put("source", "a2");
		requestData.put("target", "a3");
		Long id = chessGameService.create();
		given()
				.body(requestData)
				.contentType(ContentType.JSON)
		.when()
				.put("/rooms/" + id + "/board")
		.then()
				.log().all()
				.statusCode(200);
	}
}