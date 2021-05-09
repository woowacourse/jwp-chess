package chess.controller.web;

import chess.controller.web.dto.game.RoomRequestDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

class RoomRestControllerTest extends AcceptanceTest {

    @Test
    @DisplayName("체스 게임 생성 요청, method = post, path = /games")
    void createRoom() {
        RoomRequestDto roomRequestDto =
                new RoomRequestDto("user3", "1234", "roomName2");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(roomRequestDto)
                .when()
                .post("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("gameId", notNullValue());
    }

    @Test
    @DisplayName("게임 진행 상태인 방들을 조회해온다, method = get, path = /rooms/playing")
    void findPlayingGames() {
        createNewRoom();

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/rooms/playing")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("size()", is(2));
    }

    @Test
    @DisplayName("게임 방을 선택해서 입장한다, method = get, path = /rooms/{roomId}/join")
    void joinRoom() {
        // given
        createNewRoom();
        Map<String, String> params = new HashMap<>();
        params.put("username", "유저이름");
        params.put("password", "1234");

        RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/rooms/1/join")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("roomId", notNullValue());
    }

    private void createNewRoom() {
        RoomRequestDto roomRequestDto =
                new RoomRequestDto("user3", "1234", "roomName2");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(roomRequestDto)
                .when()
                .post("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("gameId", notNullValue());
    }
}