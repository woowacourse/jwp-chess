package wooteco.chess;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RoomControllerTest {
    @LocalServerPort
    int port;

    @BeforeEach
    void setPort() {
        RestAssured.port = port;
    }

    @DisplayName("첫 화면에 대한 요청에 정상적으로 응답하는지 확인")
    @Test
    void validateResponseToIndexPage() {
        given().
                log().all().
                when().
                get("/").
                then().
                log().all().
                statusCode(200).
                body(containsString(":: 체 스 게 임 방 목 록 ::"));
    }

    @DisplayName("새 게임 방을 생성하면 리다이렉트를 제대로 수행하는지 확인")
    @Test
    void validateResponseToCreateRoom() {
        String name = "우테코 체스";

        given().
                log().all().
                contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8").
                formParam("name", name).
                when().
                post("/new").
                then().
                log().all().
                statusCode(302);
    }
}
