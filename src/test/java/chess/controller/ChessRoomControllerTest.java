package chess.controller;

import chess.dto.request.RoomRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ChessRoomControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("게임방을 생성 할 수 있는 홈페이지를 보여준다.")
    @Test
    void home() {
        RestAssured.given().log().all()
                .accept(MediaType.TEXT_HTML_VALUE)
                .when().get("/")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    //TODO: body 테스트를 어떻게 해야할지 질문 드려야함
    @DisplayName("새로운 체스방을 생성한다.")
    @Test
    void create() {
        RoomRequest roomRequest = new RoomRequest("하이체스", "1234");

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(roomRequest)
                .when().post("/create")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
