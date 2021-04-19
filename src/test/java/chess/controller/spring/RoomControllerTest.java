package chess.controller.spring;

import chess.dto.RoomDTO;
import chess.service.spring.RoomService;
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

    @Autowired
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("방 목록을 조회한다.")
    @Order(1)
    @Test
    void findAllRooms() throws JsonProcessingException {
        roomService.addRoom("room1");
        roomService.addRoom("room2");
        String expectedResponseBody = writeResponse(Arrays.asList(new RoomDTO(1, "room1"), new RoomDTO(2, "room2")));

        Response response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/rooms");

        assertResponse(response, expectedResponseBody);
    }

    private String writeResponse(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }

    private void assertResponse(Response response, String expectedResponseBody) {
        response.then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body(is(expectedResponseBody));
    }

    @DisplayName("방을 추가한다.")
    @Order(2)
    @Test
    void addRoom() throws JsonProcessingException {
        String expectedResponseBody = writeResponse(new RoomDTO(3, "room3"));

        Response response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("room3")
                .when().post("/rooms");

        assertResponse(response, expectedResponseBody);
    }
}
