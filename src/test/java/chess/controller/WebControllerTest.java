package chess.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("초기 화면 테스트")
    @Test
    void index() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .contentType(MediaType.TEXT_HTML_VALUE)
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("게임 redirect 테스트")
    @Test
    void redirect() {
        assertThat(RestAssured.given().redirects().follow(false).log().all()
                .when().get("/game/?gameId=1")
                .then().log().all()
                .statusCode(302)
                .extract()
                .header("Location")
                .contains("/game/1")).isTrue();
    }

    @DisplayName("게임 방 입장 테스트")
    @Test
    void enterGame() {
        RestAssured.given().log().all()
                .when().get("/game/1")
                .then().log().all()
                .contentType(MediaType.TEXT_HTML_VALUE)
                .statusCode(HttpStatus.OK.value());
    }
}
