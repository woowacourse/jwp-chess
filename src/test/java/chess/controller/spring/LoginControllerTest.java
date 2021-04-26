package chess.controller.spring;

import chess.dto.LoginRequestDTO;
import chess.service.spring.RoomService;
import chess.service.spring.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext
@ActiveProfiles("test")
class LoginControllerTest {

    @LocalServerPort
    int port;

    private int roomId;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        roomId = roomService.addRoom("room", "pass1");
    }

    @AfterEach
    void tearDown() {
        userService.deleteAllByRoomId(roomId);
        roomService.deleteById(roomId);
    }

    @DisplayName("로그인을 시도한다.")
    @Test
    void login() throws JsonProcessingException {
        String requestBody = writeHttpBody(roomId, "abc123");

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    private String writeHttpBody(int roomId, String password) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(new LoginRequestDTO(roomId, password));
    }

    @DisplayName("이미 방에 두 명의 플레이어가 존재하는 경우 유저가 추가될 수 없다.")
    @Test
    void cannotLogin() throws JsonProcessingException {
        userService.addUserIntoRoom(roomId, "pass1");
        String requestBody = writeHttpBody(roomId, "abc123");

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
