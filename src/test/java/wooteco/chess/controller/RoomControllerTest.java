package wooteco.chess.controller;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomControllerTest {
	@LocalServerPort
	int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
	}

	@DisplayName("루트 URL 접근 테스트")
	@Test
	void test5() {
		given().
			log().all().
			when().
			get("/").
			then().
			log().all().
			statusCode(200).
			body(containsString(":: 체 스 게 임 방 목 록 ::"));
	}

	@DisplayName("방 생성 테스트, redirect가 되는지 확인")
	@Test
	void name() {
		given().
			contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8").
			log().
			all().
			formParam("name", "테스트 제목").
			when().
			post("/new").
			then().
			log().all().
			statusCode(302);
	}
}
