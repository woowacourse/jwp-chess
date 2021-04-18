package chess.spring.controller;

import chess.domain.board.ChessBoard;
import chess.domain.piece.TeamType;
import chess.dto.MoveRequestDTO;
import chess.dto.board.BoardDTO;
import chess.service.spring.ChessService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.core.Is.is;

@DisplayName("ChessController HTTP Method Test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ChessControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private ChessService chessService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        chessService.deleteAllHistories();
    }

    @DisplayName("보드를 조회한다.")
    @Test
    void showBoard() throws JsonProcessingException {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/chessgame/chessboard")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3))
                .body(is(parseExpectedResponse()));
    }

    @DisplayName("체스 기물을 이동한다.")
    @Test
    void move() throws JsonProcessingException {
        MoveRequestDTO moveRequestDTO = new MoveRequestDTO("a2", "a3", "WHITE");
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(moveRequestDTO)
                .when().put("/chessgame/chessboard")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3))
                .body(is(parseExpectedResponse()));
    }

    private String parseExpectedResponse() throws JsonProcessingException {
        ChessBoard chessBoard = chessService.findChessBoard();
        TeamType currentTeamType = chessService.findCurrentTeamType();
        BoardDTO boardDTO = BoardDTO.of(chessBoard, currentTeamType);
        return new ObjectMapper().writeValueAsString(boardDTO);
    }

    @DisplayName("리스타트시 응답 결과는 메인 페이지 url이다.")
    @Test
    void restart() {
        RestAssured.given().log().all()
                .when().delete("/chessgame/histories")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body(is("/"));
    }

    @DisplayName("도메인 예외가 발생하면 500 에러와 메시지가 발생한다.")
    @Test
    void handleException() {
        RestAssured.given().log().all()
                .when().get("/chessgame/result")
                .then().log().all()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(is("승리한 팀을 찾을 수 없습니다."));
    }
}
