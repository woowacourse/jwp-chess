package chess.web;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;

import chess.repository.RoomDao;
import io.restassured.RestAssured;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

class RoomControllerTest extends SpringBootTestConfig {

    @Autowired
    private RoomDao roomDao;

    @DisplayName("유효한 이름을 받으면 게임방 생성")
    @Test
    void createRoom() {
        final String name = "summer";
        final String password = "password";

        RestAssured.given().log().all()
                .formParams(Map.of("name", name, "password", password))
                .when().post("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("url", containsString("/rooms/"));
    }

    @DisplayName("부적절한 이름이 입력되면 400 에러 발생")
    @ParameterizedTest
    @ValueSource(strings = {"", "16자를넘는방이름은안되니까돌아가"})
    void nameException(String name) {
        final String password = "password";

        RestAssured.given().log().all()
                .formParams("name", name, "password", password)
                .when().post("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

//    @DisplayName("존재하는 모든 방 조회")
//    @Test
//    void findRooms() {
//        roomDao.save("does", "password");
//        RestAssured.given().log().all()
//                .when().get("/rooms")
//                .then().log().all()
//                .statusCode(HttpStatus.OK.value())
//                .body("size()", is(2));
//    }
//
//    @DisplayName("존재하는 방에 대한 새로운 보드 생성 요청")
//    @Test
//    void newGame() {
//        RestAssured.given().log().all()
//                .when().get("/rooms/1/new")
//                .then().log().all()
//                .statusCode(HttpStatus.OK.value());
//    }

}
