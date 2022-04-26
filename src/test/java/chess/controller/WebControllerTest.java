package chess.controller;

import chess.dao.GameDao;
import io.restassured.RestAssured;
import io.restassured.config.EncoderConfig;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    GameDao gameDao;

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
        RestAssured.given()
                .config(RestAssured.config().encoderConfig(EncoderConfig.encoderConfig()
                        .encodeContentTypeAs(MediaType.MULTIPART_FORM_DATA_VALUE, ContentType.TEXT)))
                .formParam("gameName", "name")
                .formParam("password", "password")
                .redirects().follow(false).log().all()
                .when().get("/game")
                .then().log().all()
                .statusCode(302)
                .extract();

        gameDao.delete(gameDao.find("name", "password").get());
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
