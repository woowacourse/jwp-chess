package chess.advice;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.*;

import chess.domain.Room;
import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChessAdviceTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("에러 테스트")
    void illegalArgumentExceptionHandler() {
        Room room = new Room(1, "멍토", "비번", 1);

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(room)
            .when().post("/api/rooms/2")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}