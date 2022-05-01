package chess;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChessStaticResourceControllerTest {

    @LocalServerPort
    int port;

    @Test
    @DisplayName("기본 경로 페이지 반환을 검증한다.")
    void index() {
        RestAssured.given().log().all()
            .when().get("/")
            .then().log().all()
            .statusCode(HttpStatus.OK.value())
            .contentType(MediaType.TEXT_HTML_VALUE);
    }
}
