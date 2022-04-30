package chess.web;

import static org.hamcrest.core.StringContains.containsString;

import io.restassured.RestAssured;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SpringChessControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    ApplicationContext context;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("유효한 이름을 받으면 게임방 입장")
    @Test
    void createRoom() {
        final String name = "summer";
        final String password = "password";

        RestAssured.given().log().all()
                .formParams(Map.of("name", name, "password", password))
                .when().post("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("url", containsString("/rooms/1"));
    }

    @DisplayName("부적절한 이름이 입력되면 400 에러 발생")
    @ParameterizedTest
    @ValueSource(strings = {"", "16자를넘는방이름은안되니까돌아가"})
    void nameException(String name) {
        final String password = "password";

        RestAssured.given().log().all()
                .formParam("name", name, "password", password)
                .when().post("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

}