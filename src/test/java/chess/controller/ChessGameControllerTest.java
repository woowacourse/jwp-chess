package chess.controller;

import chess.controller.dto.request.PieceMoveRequest;
import chess.controller.dto.request.PromotionRequest;
import chess.dao.ChessGameDao;
import chess.dao.PieceDao;
import chess.domain.ChessGame;
import chess.domain.Position;
import chess.domain.piece.PieceFactory;
import chess.domain.state.Turn;
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

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("새로운 게임 생성")
    void createNewGame() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("chessgames")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", Matchers.matchesRegex("/chessgames/[0-9+]"));
    }

    @Test
    @DisplayName("체스 보드 로딩")
    void loadChessGame() {
        ChessGame chessGame = new ChessGame(Turn.WHITE_TURN.name(), "title", "password");
        long chessGameId = chessGameDao.createChessGame(chessGame)
                .getId();

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("chessgames/" + chessGameId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("체스 기물 이동")
    void movePiece() {
        ChessGame chessGame = new ChessGame(Turn.WHITE_TURN.name(), "title", "password");
        long chessGameId = chessGameDao.createChessGame(chessGame)
                .getId();
        pieceDao.savePieces(chessGameId, PieceFactory.createNewChessBoard());

        RestAssured.given().log().all()
                .body(new PieceMoveRequest("a2", "a4"))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("chessgames/" + chessGameId + "/move")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("체스 기물 프로모션")
    void promotionPiece() {
        ChessGame chessGame = new ChessGame(Turn.WHITE_TURN.name(), "title", "password");
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
                .when().post("chessgames/" + chessGameId + "/promotion")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("체스 점수 반환")
    void calculateScore() {
        ChessGame chessGame = new ChessGame(Turn.WHITE_TURN.name(), "title", "password");
        long chessGameId = chessGameDao.createChessGame(chessGame)
                .getId();

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("chessgames/" + chessGameId + "/score")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("게임 종료 여부 판별")
    void chessGameStatus() {
        ChessGame chessGame = new ChessGame(Turn.WHITE_TURN.name(), "title", "password");
        long chessGameId = chessGameDao.createChessGame(chessGame)
                .getId();

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("chessgames/" + chessGameId + "/status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("게임 우승자 반환")
    void chessGameWinner() {
        ChessGame chessGame = new ChessGame(Turn.WHITE_TURN.name(), "title", "password");
        long chessGameId = chessGameDao.createChessGame(chessGame)
                .getId();
        pieceDao.savePieces(chessGameId, PieceFactory.createNewChessBoard());
        pieceDao.delete(chessGameId, Position.from("e8"));

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("chessgames/" + chessGameId + "/winner")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
