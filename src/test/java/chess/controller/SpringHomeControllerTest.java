package chess.controller;

import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@SpringBootTest
class SpringHomeControllerTest {

	@DisplayName("홈 화면 테스트")
	@Test
	void home() {
		given().log().all()
				.when().get("/chess/home")
				.then().log().all()
				.statusCode(200)
				.body(containsString("게임하러 가기"));
	}
}