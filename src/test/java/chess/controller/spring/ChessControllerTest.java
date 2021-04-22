package chess.controller.spring;

import chess.domain.board.ChessBoard;
import chess.domain.piece.TeamType;
import chess.dto.MoveRequestDTO;
import chess.dto.board.BoardDTO;
import chess.service.spring.ChessService;
import chess.service.spring.RoomService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
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

import static org.hamcrest.core.Is.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext
@ActiveProfiles("test")
class ChessControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private ChessService chessService;

    @Autowired
    private RoomService roomService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        roomService.addRoom("room1");
    }

    @DisplayName("보드를 조회한다.")
    @Test
    void showBoard() throws JsonProcessingException {
        String expectedResponseBody = writeResponseBody();

        Response response = RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/chessgame/1/chessboard");

        assertResponse(response, HttpStatus.OK, expectedResponseBody);
    }

    private String writeResponseBody() throws JsonProcessingException {
        ChessBoard chessBoard = chessService.findChessBoardByRoomId(1);
        TeamType currentTeamType = chessService.findCurrentTeamTypeByRoomId(1);
        BoardDTO boardDTO = BoardDTO.of(chessBoard, currentTeamType);
        return new ObjectMapper().writeValueAsString(boardDTO);
    }

    private void assertResponse(Response response, HttpStatus httpStatus, String expectedResponseBody) {
        response.then().log().all()
                .statusCode(httpStatus.value())
                .body(is(expectedResponseBody));
    }

    @DisplayName("체스 기물을 이동한다.")
    @Test
    void move() throws JsonProcessingException {
        MoveRequestDTO moveRequestDTO = new MoveRequestDTO("a2", "a3", "WHITE");

        Response response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(moveRequestDTO)
                .when().put("/chessgame/1/chessboard");

        assertResponse(response, HttpStatus.BAD_REQUEST, "혼자서는 플레이할 수 없습니다.");
    }

    @DisplayName("현재 턴이 아닌 기물을 조작시 예외가 발생한다. (혼자서는 플레이가 불가능하다.)")
    @Test
    void cannotMove() {
        MoveRequestDTO moveRequestDTO = new MoveRequestDTO("a2", "a3", "BLACK");

        Response response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(moveRequestDTO)
                .when().put("/chessgame/1/chessboard");

        assertResponse(response, HttpStatus.BAD_REQUEST, "혼자서는 플레이할 수 없습니다.");
    }

    @DisplayName("리스타트시 응답 결과는 메인 페이지 url이다.")
    @Test
    void restart() {
        Response response = RestAssured.given().log().all()
                .when().delete("/chessgame/1");

        assertResponse(response, HttpStatus.OK, "/");
    }

    @DisplayName("도메인 예외가 발생하면 500 에러와 메시지가 발생한다.")
    @Test
    void handleException() {
        Response response = RestAssured.given().log().all()
                .when().get("/chessgame/1/result");

        assertResponse(response, HttpStatus.BAD_REQUEST, "승리한 팀을 찾을 수 없습니다.");
    }
}
