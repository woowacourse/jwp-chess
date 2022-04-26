package chess.controller;

import chess.controller.dto.request.ChessGameRoomDeleteRequest;
import chess.dao.ChessGameDao;
import chess.domain.ChessGameRoom;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ChessGameRoomControllerTest {

    @Autowired
    private ChessGameDao chessGameDao;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("모든 체스방의 리스트 반환")
    void findAllChessGameRoom() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/chessgames")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("체스 게임 삭제")
    void deleteChessGameRoom() {
        String password = "password";
        long chessGameId = chessGameDao.createChessGame(new ChessGameRoom("title", password));

        RestAssured.given().log().all()
                .body(new ChessGameRoomDeleteRequest(password))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/chessgames/" + chessGameId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
