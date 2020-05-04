package wooteco.chess.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;

@Execution(ExecutionMode.CONCURRENT)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IndexControllerTest {
	@LocalServerPort
	int port;

	@BeforeEach
	void setup() {
		RestAssured.port = port;
	}

	@DisplayName("방을 생성하는 화면으로 접속")
	@Test
	void indexRequest() {
		given().
				log().all().
				when().
				get("/").
				then().
				log().all().
				body(containsString("방 만들기")).
				statusCode(200);
	}

	@DisplayName("방이 존재하지 않을 때 방 만들기 화면으로 이동")
	@Test
	void roomRequest_roomIsNotExist_redirectToIndexPage() {
		given().
				log().all().
				when().
				get("/rooms/123").
				then().
				log().all().
				body(containsString("방 만들기")).
				statusCode(200);
	}
}