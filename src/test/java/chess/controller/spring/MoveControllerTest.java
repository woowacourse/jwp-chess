package chess.controller.spring;

import chess.dto.GridDto;
import chess.dto.PieceDto;
import chess.dto.requestdto.MoveRequestDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class MoveControllerTest {
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
    @DisplayName("/move POST")
    void move() throws JsonProcessingException {
        String roomName = "testroom";
        Response response = RestAssured
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(roomName)
                .when()
                    .get("/grid/{roomName}", roomName)
                .then()
                    .statusCode(HttpStatus.OK.value())
                    .extract()
                    .response();
        ObjectMapper mapper = new ObjectMapper();
        GridDto gridDto = mapper.readValue(
                mapper.writeValueAsString(response.path("data.gridDto")),
                GridDto.class
        );
        List<PieceDto> piecesDto = mapper.readValue(
                mapper.writeValueAsString(response.path("data.piecesResponseDto")),
                new TypeReference<List<PieceDto>>(){}
        );
        String sourcePosition = "a2";
        String targetPosition = "a4";

        RestAssured
                .given()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(new MoveRequestDto(piecesDto, sourcePosition, targetPosition, gridDto))
                .when()
                    .post("/move")
                .then()
                    .statusCode(HttpStatus.OK.value());
    }
}

