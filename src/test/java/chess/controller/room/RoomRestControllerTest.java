package chess.controller.room;

import chess.mysql.room.RoomDao;
import chess.service.RoomService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomRestControllerTest {
    @Autowired
    RoomService roomService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        roomService.createRoom("테스트", "1111");
    }

    @DisplayName("활성화 중인 게임을 가져올 수 있는지 확인")
    @Test
    void getGamesTest() {
        RestAssured
                .when().get("/room")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .assertThat().body("activeRooms", not(nullValue()));
    }

    @DisplayName("방에 입장할 때 비밀번호 입력기능 확인")
    @Test
    void enterPassword() {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UserRequestDto(1, "1111"))
                .when().post("/room/1/password")
                .then().statusCode(HttpStatus.OK.value());
    }

    @DisplayName("비밀번호 입력 시 방 번호가 없을 때 예외가 발생하는지")
    @Test
    void whenFalseRoomId() {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UserRequestDto(1, "1111"))
                .when().post("/room/999/password")
                .then().statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("유저가 가득찬 방일 때 비밀번호를 추가로 입력하려고 하면 예외가 발생하는지")
    @Test
    void whenRoomIsFull() {
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UserRequestDto(1, "1234"))
                .when().post("/room/1/password")
                .then().statusCode(HttpStatus.OK.value());

        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new UserRequestDto(1, "4567"))
                .when().post("/room/1/password")
                .then().statusCode(HttpStatus.BAD_REQUEST.value());
    }
}