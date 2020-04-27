package wooteco.chess.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

public class SpringChessControllerTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("get 요청을 보내고 문자열 응답 받기")
    @Test
    void test1() {
        System.out.println("테스트 시작");
        given().
                log().all().
        when().
                get("/").
        then().
                log().all().
                statusCode(200).
                body(containsString("Hello World"));
    }

}
