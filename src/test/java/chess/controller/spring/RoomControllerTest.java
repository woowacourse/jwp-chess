package chess.controller.spring;

import chess.dto.RoomDTO;
import chess.service.spring.RoomService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RoomControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private RoomService roomService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        jdbcTemplate.execute("DROP TABLE HISTORY IF EXISTS");
        jdbcTemplate.execute("DROP TABLE ROOM IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE IF NOT EXISTS ROOM" +
                "(ID   INT NOT NULL AUTO_INCREMENT," +
                "NAME VARCHAR(255)," +
                "PRIMARY KEY (ID)" +
                ");");
        roomService.addRoom("test1");
        roomService.addRoom("test2");
    }

    @DisplayName("방 목록을 조회한다.")
    @Test
    @Order(1)
    void findAllRooms() throws JsonProcessingException {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2))
                .body(is(parseExpectedResponse()));
    }

    private String parseExpectedResponse() throws JsonProcessingException {
        List<RoomDTO> rooms = Arrays.asList(new RoomDTO(1, "test1"), new RoomDTO(2, "test2"));
        return new ObjectMapper().writeValueAsString(rooms);
    }

    @DisplayName("방을 추가한다.")
    @Test
    @Order(2)
    void addRoom() throws JsonProcessingException {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("test3")
                .when().post("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2))
                .body(is(new ObjectMapper().writeValueAsString(new RoomDTO(3, "test3"))));
    }
}
