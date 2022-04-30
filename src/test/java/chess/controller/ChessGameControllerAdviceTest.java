package chess.controller;

import static chess.ChessGameFixture.createRunningChessGame;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;

import chess.controller.dto.request.ChessGamePasswordRequest;
import chess.controller.dto.request.ChessGameRequest;
import chess.controller.dto.request.PieceMoveRequest;
import chess.controller.dto.request.PromotionRequest;
import chess.dao.ChessGameDao;
import chess.domain.ChessGame;
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
class ChessGameControllerAdviceTest {

    @Autowired
    private ChessGameDao chessGameDao;

    private long chessGameId;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        ChessGame chessGame = createRunningChessGame();
        chessGameDao.deleteChessGame(chessGame);
        chessGameId = chessGameDao.createChessGame(chessGame)
                .getId();
    }

    @Test
    @DisplayName("해당 위치에 존재한 기물이 없을 때 이동시 예외 발생")
    void exceptionByEmptySource() {
        RestAssured.given().log().all()
                .body(new PieceMoveRequest("a7", "a6"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("chessgames/" + chessGameId + "/move")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", is("해당 위치에 존재하는 기물이 없습니다."));
    }

    @Test
    @DisplayName("체스게임이 존재하지 않을 때 접속 시 예외 발생")
    void exceptionByNotFoundChessGame() {
        long notFoundChessGameId = chessGameId + 1;
        RestAssured.given().log().all()
                .body(new ChessGamePasswordRequest("password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("chessgames/" + notFoundChessGameId)
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("Promotion 기물 생성 시 null입력 예외발생")
    void promotionPieceWithNullException() {
        RestAssured.given().log().all()
                .body(new PromotionRequest(null))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("chessgames/" + chessGameId + "/promotion")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", containsString("프로모션할 기물을 입력해 주세요."));
    }


    @Test
    @DisplayName("기물 움직임 시 null입력 예외발생")
    void movePieceWithNullException() {
        RestAssured.given().log().all()
                .body(new PieceMoveRequest(null, null))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("chessgames/" + chessGameId + "/move")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", containsString("초기 위치 값을 입력해 주세요."))
                .body("message", containsString("도착 위치 값을 입력해 주세요."));
    }

    @Test
    @DisplayName("체스 게임 생성 시 null입력 예외발생")
    void createChessGameWithNullException() {
        RestAssured.given().log().all()
                .body(new ChessGameRequest(null, null))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("chessgames/")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", containsString("체스 게임 제목을 입력해 주세요."))
                .body("message", containsString("체스 게임 비밀번호를 입력해 주세요."));
    }

    @Test
    @DisplayName("비밀번호 입력 시 null입력 예외발생")
    void inputPasswordWithNullException() {
        RestAssured.given().log().all()
                .body(new ChessGamePasswordRequest(null))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("chessgames/" + chessGameId)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("message", containsString("비밀번호를 입력해 주세요."));
    }
}
