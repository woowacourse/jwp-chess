package chess.controller;

import static org.hamcrest.core.StringContains.containsString;

import chess.service.ChessService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestPropertySource("classpath:application-test.properties")
@Sql("classpath:schema.sql")
class ChessControllerTest {

    @LocalServerPort
    private int port;

    private final ChessService chessService;

    @Autowired
    public ChessControllerTest(ChessService chessService) {
        this.chessService = chessService;
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("index page를 조회한다.")
    @Test
    void index() {
        RestAssured.given().log().all()
            .accept(MediaType.TEXT_HTML_VALUE)
            .when().get("/")
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("game page를 조회한다.")
    @Test
    void gamePage() {
        Long id = chessService.createGame("first", "1234");

        RestAssured.given().log().all()
            .accept(MediaType.TEXT_HTML_VALUE)
            .when().get("/game/" + id)
            .then().log().all()
            .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("game page 조회를 실패한다.")
    @Test
    void gamePageNotFound() {
        RestAssured.given().log().all()
            .accept(MediaType.TEXT_HTML_VALUE)
            .when().get("/game/" + 500)
            .then().log().all()
            .body("html.body.div.h2", containsString("페이지를 찾을 수 없습니다"));
    }

    @DisplayName("game을 생성한다.")
    @Test
    void createGame() {
        String name = "승팡 체스";
        String password = "1234";

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .formParam("name", name)
            .formParam("password", password)
            .when().post("/game")
            .then().log().all()
            .statusCode(HttpStatus.FOUND.value())
            .header("Location", containsString("/game"));
    }

    @AfterEach
    void cleanUp() {
        chessService.removeAllGame();
    }
}
