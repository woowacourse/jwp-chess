package chess.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@SpringBootTest
class SpringRoomControllerTest {

	@Test
	@DisplayName("체스 게임 화면 테스트")
	void load() {
		given().log().all()
				.when().get("/chess/rooms/" + 1)
				.then().log().all()
				.statusCode(200)
				.body(containsString("명령"))
				.body(containsString("table"));
	}

	@Test
	@DisplayName("체스 게임 Redirection 테스트")
	void command() {
		given().log().all()
				.contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8")
				.formParam("command", "start")
				.when().post("/chess/rooms/" + 1)
				.then().log().all()
				.statusCode(302);
	}
}