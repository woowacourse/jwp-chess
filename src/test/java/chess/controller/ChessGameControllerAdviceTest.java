package chess.controller;

import static org.hamcrest.core.Is.is;

import chess.controller.dto.request.PieceMoveRequest;
import chess.dao.ChessGameDao;
import chess.domain.state.Turn;
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
class ChessGameControllerAdviceTest {

	@Autowired
	private ChessGameDao chessGameDao;

	private long chessGameId;

	@LocalServerPort
	private int port;

	@BeforeEach
	void setUp() {
		RestAssured.port = port;
		chessGameId = chessGameDao.createChessGame(Turn.WHITE_TURN);
	}

	@Test
	@DisplayName("해당 위치에 존재한 기물이 없을 때 이동시 예외 발생")
	void exceptionByEmptySource() {
		RestAssured.given().log().all()
				.body(new PieceMoveRequest("a7", "a6"))
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().post("chessgames/" + chessGameId + "/move")
				.then().log().all()
				.statusCode(HttpStatus.BAD_REQUEST.value())
				.body("message", is("해당 위치에 존재하는 기물이 없습니다."));
	}

	@Test
	@DisplayName("체스게임이 존재하지 않을 때 접속 시 예외 발생")
	void exceptionByNotFoundChessGame() {
		long notFoundChessGameId = chessGameId + 1;
		RestAssured.given().log().all()
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.when().get("chessgames/" + notFoundChessGameId)
				.then().log().all()
				.statusCode(HttpStatus.NOT_FOUND.value());
	}
}
