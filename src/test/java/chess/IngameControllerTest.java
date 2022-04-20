package chess;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.hamcrest.core.Is.is;

@SpringBootTest
class IngameControllerTest {

    @Test
    void contextLoads() {
    }

    @DisplayName("Parameter Header - Params")
    @Test
    void messageForParam() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/ingame?gameID=panda")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body(is("panda"));
    }

}
