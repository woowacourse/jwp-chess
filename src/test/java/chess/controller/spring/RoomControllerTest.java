package chess.controller.spring;

import chess.dto.requestdto.StartRequestDto;
import chess.dto.responsedto.GridAndPiecesResponseDto;
import chess.service.ChessService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class RoomControllerTest {

    @Autowired
    private ChessService chessService;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("/room/{roomId}/restart POST")
    void restart() {
        String roomName = "test";
        StartRequestDto startRequestDto = new StartRequestDto(roomName);
        GridAndPiecesResponseDto gridAndPiecesResponseDto = chessService.getGridAndPieces(startRequestDto);
        Long roomId = gridAndPiecesResponseDto.getGridDto().getRoomId();

        RestAssured
                .given()
                .log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(roomName)
                .when()
                .post("/room/" + roomId + "/restart")
                .then()
                .log().all()
                .statusCode(HttpStatus.OK.value())
                .body("code", is(200))
                .body("data.piecesResponseDto.size()", is(64))
                .body("data.gridDto.roomId", is(roomId.intValue()));
    }
}