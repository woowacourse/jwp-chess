package chess.controller.web;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.hamcrest.Matchers.*;

class RoomControllerTest {
    @DisplayName("활성화 중인 게임을 가져올 수 있는지 확인")
    @Test
    void getGamesTest() {
        RestAssured
                .when().get("/user")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("runningGames", not(nullValue()));
    }
}