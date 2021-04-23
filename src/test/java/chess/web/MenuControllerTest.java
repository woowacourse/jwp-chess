package chess.web;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;

import chess.service.ChessService;
import chess.service.RoomService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class MenuControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private ChessService service;

    @Autowired
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        roomService.add("test");
        service.restartBoardById(1);
    }

    @AfterEach
    void cleanUp() {
        roomService.deleteAll();
    }

    @DisplayName("시작할 때 체스판을 초기화 한다.")
    @Test
    void renderInitBoard() {
        RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/rooms/1/board")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.TEXT_HTML_VALUE)
            .body(containsString("WOOWA CHESS"));
    }

    @DisplayName("체스판을 불러올 수 있다.")
    @Test
    void loadBoard() {
        RestAssured.given().log().all()
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/rooms/1/board/load")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .body("size()", is(1));
    }
}
