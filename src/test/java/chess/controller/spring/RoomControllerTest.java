package chess.controller.spring;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class RoomControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @BeforeEach
    public void setup() {
        jdbcTemplate.execute("TRUNCATE TABLE piece");
        jdbcTemplate.execute("TRUNCATE TABLE grid");
        jdbcTemplate.execute("TRUNCATE TABLE room");
    }

    @Test
    @DisplayName("/room/{roomId}/restart POST")
    void restart() {
        String roomName = "test";
        Response response = createGridAndRoom(roomName);
        int roomId = response.path("data.gridDto.roomId");
        RestAssured
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(roomName)
                .when()
                    .post("/room/" + roomId + "/restart")
                .then()
                    .log().all()
                    .statusCode(HttpStatus.OK.value())
                    .body("code", is(200))
                    .body("data.piecesResponseDto.size()", is(64))
                    .body("data.gridDto.roomId", is(roomId));
    }

    @Test
    @DisplayName("/room GET")
    void getRooms() {
        String roomName = "test";
        createGridAndRoom(roomName);
        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/room")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("code", is(200))
                .body("data.rooms.size()", is(1))
                .body("data.rooms[0].roomName", is(roomName));
    }

    private Response createGridAndRoom(String roomName) {
        return RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("roomName", roomName)
                .when()
                .get("/grid/{roomName}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .response();
    }
}