package chess.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpringStatisticControllerTest {

	@Test
	@DisplayName("통계 화면 테스트")
	void load() {
		given().log().all()
				.when().get("/chess/statistics")
				.then().log().all()
				.statusCode(200)
				.body(containsString("통계"));
	}
}