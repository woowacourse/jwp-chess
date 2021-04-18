package chess.controller;

import chess.dao.ChessGameDAO;
import chess.dao.PieceDAO;
import chess.dto.ChessGameResponseDto;
import chess.service.ChessGameService;
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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.is;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChessApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ChessGameService chessGameService;

    @Autowired
    private PieceDAO pieceDAO;

    @Autowired
    private ChessGameDAO chessGameDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        pieceDAO = new PieceDAO(jdbcTemplate);
        chessGameDAO = new ChessGameDAO(jdbcTemplate);
        RestAssured.port = port;
    }

    @AfterEach
    void tearDown() {
        jdbcTemplate.execute("DELETE FROM piece");
        jdbcTemplate.execute("DELETE FROM chess_game");
    }

    @DisplayName("피스들을 조회하는 API 요청")
    @Test
    void testFindPieces() {
        //given
        chessGameService.createNewChessGame();
        String source = "a7";
        String target = "a5";

        //when //then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/pieces?source={source}&target={target}", source, target)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("pieceDtos.size()", is(32))
                .body("state", is("WhiteTurn"))
                .body("finished", is(false));
    }

    @DisplayName("체스게임을 조회하는 API 요청 ")
    @Test
    void testFindChessGame() {
        //given
        chessGameService.createNewChessGame();

        //when //then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/chessgames")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("pieceDtos.size()", is(32))
                .body("state", is("BlackTurn"))
                .body("finished", is(false));
    }

    @DisplayName("새로운 체스 게임을 만드는 API 요청")
    @Test
    void testCreateNewChessGame() {
        //when //then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/chessgames")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .body("pieceDtos.size()", is(32))
                .body("state", is("BlackTurn"))
                .body("finished", is(false));
    }

    @DisplayName("체스 게임을 종료하는 요청을 ")
    @Test
    void testEndChessGame() {
        //given
        chessGameService.createNewChessGame();

        //when //then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/chessgames")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("pieceDtos.size()", is(32))
                .body("state", is("End"))
                .body("finished", is(true));
    }

    @DisplayName("체스게임의 현재 점수를 계산하는 API 요청")
    @Test
    void testCalculateScores() {
        //given
        ChessGameResponseDto newChessGame = chessGameService.createNewChessGame();
        pieceDAO.delete(newChessGame.getChessGameId(), 1, 0);
        pieceDAO.delete(newChessGame.getChessGameId(), 7, 3);

        //when //then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/chessgames")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

}
