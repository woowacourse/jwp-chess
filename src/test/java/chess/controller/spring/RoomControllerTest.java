package chess.controller.spring;

import chess.dto.RoomDTO;
import chess.dto.RoomRegistrationDTO;
import chess.service.spring.RoomService;
import chess.service.spring.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
class RoomControllerTest {

    @LocalServerPort
    int port;

    private int firstRoomId;
    private int secondRoomId;

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        firstRoomId = roomService.addRoom("room1", "pass1");
        secondRoomId = roomService.addRoom("room2", "pass1");
    }

    @AfterEach
    void tearDown() {
        userService.deleteAllByRoomId(firstRoomId);
        userService.deleteAllByRoomId(secondRoomId);

        roomService.deleteById(firstRoomId);
        roomService.deleteById(secondRoomId);
    }

    @DisplayName("방 목록을 조회한다.")
    @Order(1)
    @Test
    void findAllRooms() throws JsonProcessingException {
        String expectedResponseBody = writeResponseBody(Arrays.asList(new RoomDTO(firstRoomId, "room1"), new RoomDTO(secondRoomId, "room2")));

        Response response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/rooms");

        assertResponse(response, expectedResponseBody, HttpStatus.OK);
    }

    private String writeResponseBody(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    private void assertResponse(Response response, String expectedResponseBody, HttpStatus httpStatus) {
        response.then().log().all()
                .statusCode(httpStatus.value())
                .body(is(expectedResponseBody));
    }

    @DisplayName("방을 추가한다.")
    @Order(2)
    @Test
    void addRoom() throws JsonProcessingException {
        String requestBody = writeResponseBody(new RoomRegistrationDTO("room3", "pass1"));
        String expectedResponseBody = writeResponseBody(new RoomDTO(secondRoomId + 1, "room3"));

        Response response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestBody)
                .when().post("/rooms");

        assertResponse(response, expectedResponseBody, HttpStatus.OK);
    }
}
