package wooteco.chess.controller;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringDefaultControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Autowired
    private SpringDefaultController springDefaultController;

    @DisplayName("SpringDefaultController가 빈으로 등록됐는지 확인")
    @Test
    void testSpringDefaultControllerIsRegisteredBean() {
        assertThat(springDefaultController).isNotNull();
    }

    @DisplayName("SpringDefaultController에 SpringRoomService가 주입됐는지 확인")
    @Test
    void testDIFromSpringDefaultControllerToSpringRoomService() {
        assertThat(springDefaultController.index()).isNotNull();
    }

    @Test
    void testIndexPage() {
        indexRequest();
    }

    private void indexRequest() {
        given().
            log().all().
        when().
            get("/").
        then().
            log().all().
            body(containsString("전체 방 목록")).
        and().
            statusCode(200).
        and().
            contentType(ContentType.HTML);
    }
}

