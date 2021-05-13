package chess.controller.web;

import chess.controller.web.dto.game.RoomJoinRequestDto;
import chess.controller.web.dto.game.RoomRequestDto;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.hamcrest.core.IsNull.notNullValue;

class RoomRestControllerTest extends AcceptanceTest {

    @Test
    @DisplayName("체스 게임 생성 요청, method = post, path = /games")
    void createRoom() {
        // given
        RoomRequestDto roomRequestDto =
                new RoomRequestDto("user3", "1234", "roomName2");

        // when
        ValidatableResponse response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(roomRequestDto)
                .when()
                .post("/rooms")
                .then().log().all();

        // then
        response
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("roomId", notNullValue())
                .body("gameId", notNullValue());
    }

    @Test
    @DisplayName("게임 진행 상태인 방들을 조회해온다, method = get, path = /rooms/playing")
    void findPlayingGames() {
        // given
        createNewRoom();

        // when
        ValidatableResponse response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/rooms/playing")
                .then().log().all();

        //then
        response
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("size()", notNullValue());
    }

    @Test
    @DisplayName("게임 방을 선택해서 입장한다, method = get, path = /rooms/{roomId}/join")
    void joinRoom() {
        // given
        ExtractableResponse<Response> createResponse = createNewRoom();
        int createRoomId = createResponse.body().jsonPath().get("roomId");

        //when
        RoomJoinRequestDto roomJoinRequestDto = new RoomJoinRequestDto("유저이름", "1234");
        ValidatableResponse response = RestAssured
                .given().log().all()
                .body(roomJoinRequestDto)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/rooms/" + createRoomId + "/join")
                .then().log().all();
        //then
        response
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("roomId", notNullValue());
    }

    private ExtractableResponse<Response> createNewRoom() {
        RoomRequestDto roomRequestDto =
                new RoomRequestDto("user3", "1234", "roomName2");
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(roomRequestDto)
                .when()
                .post("/rooms")
                .then().log().all()
                .extract();
    }
}