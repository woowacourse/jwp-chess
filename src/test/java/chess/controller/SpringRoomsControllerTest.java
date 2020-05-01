package chess.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@SpringBootTest
class SpringRoomsControllerTest {

	@Test
	@DisplayName("방 목록 화면 테스트")
	void load() {
		given().log().all()
				.when().get("/chess/rooms")
				.then().log().all()
				.statusCode(200)
				.body(containsString("방 목록"));
	}

	@Test
	@DisplayName("방 생성/삭제/입장 테스트")
	void manage() {
		given().log().all()
				.when().post("/chess/rooms")
				.then().log().all()
				.statusCode(302);
	}
}