package chess.dao;

import chess.domain.game.BoardInitializer;
import chess.domain.game.ChessBoard;
import chess.domain.member.Member;
import chess.domain.pieces.Color;
import chess.domain.pieces.Piece;
import chess.domain.position.Position;
import io.restassured.RestAssured;
import org.json.JSONException;
import org.json.JSONObject;
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
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql({"schema.sql"})
class ChessControllerTest {

    @Autowired
    WebChessBoardDao boardDao;

    @Autowired
    WebChessPositionDao positionDao;

    @Autowired
    WebChessPieceDao pieceDao;

    @LocalServerPort
    int port;

    int boardId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        ChessBoard board = boardDao.save(
                new ChessBoard("방1", Color.WHITE, List.of(new Member("쿼리치"), new Member("코린")), "111"));
        this.boardId = board.getId();
        positionDao.saveAll(boardId);
        final Map<Position, Piece> initialize = new BoardInitializer().initialize();
        for (Position position : initialize.keySet()) {
            int lastPositionId = positionDao.getIdByColumnAndRowAndBoardId(position.getColumn(), position.getRow(),
                    boardId);
            final Piece piece = initialize.get(position);
            pieceDao.save(new Piece(piece.getColor(), piece.getType(), lastPositionId));
        }
    }

    @DisplayName("이동 명령어로 말을 움직인다.")
    @Test
    void movePiece() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("source", "a2");
        json.put("target", "a4");
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(json.toString())
                .when().patch("/room/" + boardId + "/move")
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

}
