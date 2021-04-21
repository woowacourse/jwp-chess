package chess.controller.spring;

import chess.dto.LoginRequestDTO;
import chess.service.spring.RoomService;
import chess.service.spring.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class LoginControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private UserService userService;

    @Autowired
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        roomService.addRoom("room");
    }

    @DisplayName("로그인을 시도한다.")
    @Order(1)
    @Test
    void login() throws JsonProcessingException {
        String requestBody = new ObjectMapper().writeValueAsString(new LoginRequestDTO(1, "abc123"));

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("이미 방에 두 명의 플레이어가 존재하는 경우 유저가 추가될 수 없다.")
    @Order(2)
    @Test
    void cannotLogin() throws JsonProcessingException {
        userService.addUser("pass1", 1);
        String requestBody = new ObjectMapper().writeValueAsString(new LoginRequestDTO(1, "abc123"));

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when().post("/login")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
