package chess;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;

@SpringBootTest
class IngameControllerTest {

    @DisplayName("gameID에 따라 게임을 로드한다.")
    @Test
    void loadGameByGameID() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/ingame?gameID=panda")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body(is("panda"));
    }

    @DisplayName("move를 클릭하면 기물을 이동한다.")
    @Test
    void movePiece() {
        String movement = "source=a2&target=a4";
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(movement)
                .when().post("/ingame/panda")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body(containsString("/ingame/panda"));
    }
}
