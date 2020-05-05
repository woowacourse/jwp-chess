package wooteco.chess.controller;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import wooteco.chess.AbstractChessApplicationTest;

@Execution(ExecutionMode.CONCURRENT)
class SpringChessControllerTest extends AbstractChessApplicationTest {

    @LocalServerPort
    private int port;

    @Value("${chess.test.data.sample1}")
    private String gameOneId;

    @Value("${chess.test.data.sample2}")
    private String gameTwoId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("Render: index.hbs 렌더링이 된다")
    @Test
    void index() {
        given()
            .get("/")
            .then()
            .statusCode(200)
            .body(containsString("우아한테크체스"));
    }

    @DisplayName("Board: move가 개별적으로 동작한다")
    @Test
    void moveGameOne() {
        assertThat(moveRequest(gameOneId)).isTrue();
    }

    @DisplayName("Board: move가 개별적으로 동작한다")
    @Test
    void moveGameTwo() {
        assertThat(moveRequest(gameTwoId)).isTrue();
    }

    private boolean moveRequest(String gameId) {
        return given()
            .contentType(ContentType.JSON)
            .body("{\n"
                + "    \"from\": \"b2\",\n"
                + "    \"to\": \"b4\"\n"
                + "}")
            .post("/boards/" + gameId + "/move")
            .then()
            .statusCode(200)
            .extract()
            .as(boolean.class);
    }
}
