package chess.dao;

import chess.domain.game.ChessBoard;
import chess.domain.member.Member;
import chess.domain.pieces.Color;
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
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"schema.sql"})
class ChessControllerTest {

    @Autowired
    WebChessBoardDao boardDao;

    @Autowired
    WebChessPositionDao positionDao;

    @LocalServerPort
    int port;

    int boardId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        ChessBoard board = boardDao.save(
                new ChessBoard("방1", Color.WHITE, List.of(new Member("쿼리치"), new Member("코린"))));
        this.boardId = board.getId();
        positionDao.saveAll(boardId);
    }

    @DisplayName("이동 명령어로 말을 움직인다.")
    @Test
    void movePiece() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body("command=move a2 a4")
                .when().post("/room/" + boardId + "/move")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("현재 점수를 보여준다.")
    @Test
    void showStatus() {
        RestAssured.given().log().all()
                .when().get("/room/" + boardId + "/status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @AfterEach
    void setDown() {
        boardDao.deleteAll();
        positionDao.deleteAll();
    }
}
