package chess.controller;

import chess.controller.dto.PieceMoveRequest;
import chess.controller.dto.PromotionRequest;
import chess.dao.PieceDao;
import chess.domain.Position;
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
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ChessGameControllerTest {

	@Autowired
	private PieceDao pieceDao;

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
				.header("Location", "/chessgames/1");
	}

	@Test
	@DisplayName("체스 보드 로딩")
	void loadChessGame() {
		createNewGame();

		RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().get("chessgames/1")
				.then().log().all()
				.statusCode(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("체스 기물 이동")
	void movePiece() {
		createNewGame();

		RestAssured.given().log().all()
				.body(new PieceMoveRequest("a2", "a4"))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("chessgames/1/move")
				.then().log().all()
				.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	@DisplayName("체스 기물 프로모션")
	void promotionPiece() {
		createNewGame();
		Position source = Position.from("a2");
		Position target = Position.from("a8");
		pieceDao.delete(target);
		pieceDao.updatePiecePosition(source, target);

		RestAssured.given().log().all()
				.body(new PromotionRequest("Q"))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("chessgames/1/promotion")
				.then().log().all()
				.statusCode(HttpStatus.NO_CONTENT.value());
	}

	@Test
	@DisplayName("체스 점수 반환")
	void calculateScore() {
		createNewGame();

		RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().get("chessgames/1/score")
				.then().log().all()
				.statusCode(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("게임 종료 여부 판별")
	void chessGameStatus() {
		createNewGame();

		RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().get("chessgames/1/status")
				.then().log().all()
				.statusCode(HttpStatus.OK.value());
	}

	@Test
	@DisplayName("게임 우승자 반환")
	void chessGameWinner() {
		createNewGame();
		pieceDao.delete(Position.from("e8"));

		RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().get("chessgames/1/winner")
				.then().log().all()
				.statusCode(HttpStatus.OK.value());
	}
}
