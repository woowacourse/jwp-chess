package chess.controller;

import static chess.ChessGameFixture.createRunningChessGame;
import static chess.domain.state.Turn.END;
import static org.hamcrest.core.Is.is;

import chess.controller.dto.request.ChessGamePasswordRequest;
import chess.controller.dto.request.ChessGameRequest;
import chess.controller.dto.request.PieceMoveRequest;
import chess.controller.dto.request.PromotionRequest;
import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.PieceFactory;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
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
class ChessGameControllerTest {

    @Autowired
    private PieceDao pieceDao;

    @Autowired
    private ChessGameDao chessGameDao;

    @LocalServerPort
    private int port;

    private ChessGame chessGame;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        chessGame = createRunningChessGame();
        chessGameDao.deleteChessGame(chessGame);
    }

    @Test
    @DisplayName("새로운 게임 생성")
    void createNewGame() {
        RestAssured.given().log().all()
                .body(new ChessGameRequest("title", "password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("chessgames")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", Matchers.matchesRegex("/chessgames/[0-9+]"));
    }

    @Test
    @DisplayName("생성된 체스 게임 모두 반환")
    void findAllChessGames() {
        chessGameDao.createChessGame(chessGame);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("chessgames")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(1));
    }

    @Test
    @DisplayName("체스 게임 초기상태 일때 체스 보드 로딩")
    void loadChessGame() {
        long chessGameId = chessGameDao.createChessGame(chessGame)
                .getId();
        pieceDao.savePieces(chessGameId, PieceFactory.createNewChessBoard());

        RestAssured.given().log().all()
                .body(new ChessGamePasswordRequest("password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("chessgames/" + chessGameId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(32));
    }

    @Test
    @DisplayName("체스 게임 삭제")
    void deleteChessGame() {
        long chessGameId = chessGameDao.createChessGame(chessGame)
                .getId();
        chessGameDao.changeChessGameTurn(chessGameId, END);

        RestAssured.given().log().all()
                .body(new ChessGamePasswordRequest("password"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("chessgames/" + chessGameId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("체스 기물 이동")
    void movePiece() {
        long chessGameId = chessGameDao.createChessGame(chessGame)
                .getId();
        pieceDao.savePieces(chessGameId, PieceFactory.createNewChessBoard());

        RestAssured.given().log().all()
                .body(new PieceMoveRequest("a2", "a4"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("chessgames/" + chessGameId + "/move")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("체스 기물 프로모션")
    void promotionPiece() {
        long chessGameId = chessGameDao.createChessGame(chessGame)
                .getId();
        pieceDao.savePieces(chessGameId, PieceFactory.createNewChessBoard());
        Position source = Position.from("a2");
        Position target = Position.from("a8");
        pieceDao.delete(chessGameId, target);
        pieceDao.updatePiecePosition(chessGameId, source, target);

        RestAssured.given().log().all()
                .body(new PromotionRequest("Q"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("chessgames/" + chessGameId + "/promotion")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("체스 게임 종료")
    void endChessGame() {
        long chessGameId = chessGameDao.createChessGame(chessGame)
                .getId();

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().patch("chessgames/" + chessGameId + "/end")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("체스 점수 반환")
    void calculateScore() {
        long chessGameId = chessGameDao.createChessGame(chessGame)
                .getId();

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("chessgames/" + chessGameId + "/score")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2));
    }

    @Test
    @DisplayName("게임이 아직 진행중 일때 게임 종료 여부 판별")
    void chessGameStatus() {
        long chessGameId = chessGameDao.createChessGame(chessGame)
                .getId();

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("chessgames/" + chessGameId + "/status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("end", is(false));
    }

    @Test
    @DisplayName("화이트 팀이 이기고 있을 때 게임 우승자 반환")
    void chessGameWinner() {
        long chessGameId = chessGameDao.createChessGame(chessGame)
                .getId();
        pieceDao.savePieces(chessGameId, PieceFactory.createNewChessBoard());
        pieceDao.delete(chessGameId, Position.from("e8"));

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("chessgames/" + chessGameId + "/winner")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("winner", is("WHITE"));
    }
}
