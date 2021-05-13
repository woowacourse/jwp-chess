package chess.controller.web;

import chess.controller.web.dto.game.RoomRequestDto;
import chess.controller.web.dto.move.MoveRequestDto;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;

class GameRestControllerTest extends AcceptanceTest {

    @Test
    @DisplayName("체스 게임 보드 정보(체스말들의 정보) 가져오기, method = get, path = /games/1/pieces")
    void findPiecesByGameId() {
        //given
        ExtractableResponse<Response> createResponse = createRoom();
        int gameId = createResponse.body().jsonPath().get("gameId");

        //when
        ValidatableResponse response = RestAssured
                .given().log().all()
                .when()
                .get("/games/" + gameId + "/pieces")
                .then().log().all();

        //then
        response
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("size()", is(64));
    }

    @Test
    @DisplayName("체스 게임의 기록 불러오기, method = get, path = /games/1/history")
    void findHistoryByGameId() {
        //given
        ExtractableResponse<Response> createResponse = createRoom();
        int gameId = createResponse.body().jsonPath().get("gameId");

        //when
        ValidatableResponse response = RestAssured
                .given().log().all()
                .when()
                .get("/games/" + gameId + "/history")
                .then().log().all();

        //then
        response
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("source 위치에 체스말 이동 가능 경로 불러온다, method = get, path = /games/1/path?source=c2")
    void movablePath() {
        //given
        ExtractableResponse<Response> createResponse = createRoom();
        String responseSessionId = createResponse.sessionId();
        int gameId = createResponse.body().jsonPath().get("gameId");

        //when
        ValidatableResponse response = RestAssured
                .given().log().all()
                .when()
                .sessionId("JSESSIONID", responseSessionId)
                .get("/games/" + gameId + "/path?source=c2")
                .then().log().all();

        //then
        response
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("size()", notNullValue());

    }

    @Test
    @DisplayName("source 와 target 으로 체스말 이동 요청한다, method = post, path = /games/1/move")
    void move() {
        //given
        ExtractableResponse<Response> createResponse = createRoom();
        int gameId = createResponse.body().jsonPath().get("gameId");

        //when
        MoveRequestDto moveRequestDto = new MoveRequestDto("a2", "a3");
        ValidatableResponse response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(moveRequestDto)
                .when()
                .post("/games/" + gameId + "/move")
                .then().log().all();


        //then
        response
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("source", notNullValue())
                .body("target", notNullValue())
                .body("turnOwner", notNullValue())
                .body("turnNumber", notNullValue())
                .body("playing", notNullValue());
    }

    private ExtractableResponse<Response> createRoom() {
        RoomRequestDto roomRequestDto =
                new RoomRequestDto("user1", "1234", "roomName1");
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