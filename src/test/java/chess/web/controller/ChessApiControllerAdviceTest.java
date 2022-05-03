package chess.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.board.piece.Empty;
import chess.board.piece.Pawn;
import chess.board.piece.Piece;
import chess.board.piece.position.Position;
import chess.web.service.ChessService;
import chess.web.service.dto.MoveDto;
import io.restassured.RestAssured;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChessApiControllerAdviceTest {

    @LocalServerPort
    int port;

    @Autowired
    private ChessService chessService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("기물이 이동할 수 없는 경우 400 상태 코드를 보내고 이동하지 않는다.")
    void moveFail() {
        final String title = "title";
        final String password = "password";
        Long id = chessService.createBoard(title, password);
        String fromPosition = "a2";
        String toPosition = "h6";
        MoveDto moveDto = new MoveDto(fromPosition, toPosition);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(moveDto)
                .when().patch("/api/boards/" + id)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400))
                .body("errorMessage", equalTo("[ERROR] 움직일수 없습니다."));

        List<Piece> savedPieces = chessService.loadGame(id).getPieces().getPieces();
        Optional<Piece> pieceByFromPosition = findByPosition(savedPieces, Position.from(fromPosition));
        Optional<Piece> pieceByToPosition = findByPosition(savedPieces, Position.from(toPosition));

        assertAll(
                () -> assertThat(pieceByFromPosition).isPresent(),
                () -> assertThat(pieceByToPosition).isPresent(),
                () -> assertThat(pieceByFromPosition.get()).isInstanceOf(Pawn.class),
                () -> assertThat(pieceByToPosition.get()).isInstanceOf(Empty.class)
        );
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("상대 기물을 이동하는 경우 400 상태 코드를 보내고 이동하지 않는다.")
    void moveAnotherTeam() {
        final String title = "title";
        final String password = "password";
        Long id = chessService.createBoard(title, password);
        String fromPosition = "a7";
        String toPosition = "a6";
        MoveDto moveDto = new MoveDto(fromPosition, toPosition);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(moveDto)
                .when().patch("/api/boards/" + id)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400))
                .body("errorMessage", equalTo("[ERROR] 현재 차례가 아닙니다."));

        List<Piece> savedPieces = chessService.loadGame(id).getPieces().getPieces();
        Optional<Piece> pieceByFromPosition = findByPosition(savedPieces, Position.from(fromPosition));
        Optional<Piece> pieceByToPosition = findByPosition(savedPieces, Position.from(toPosition));
        assertAll(
                () -> assertThat(pieceByFromPosition).isPresent(),
                () -> assertThat(pieceByToPosition).isPresent(),
                () -> assertThat(pieceByFromPosition.get()).isInstanceOf(Pawn.class),
                () -> assertThat(pieceByToPosition.get()).isInstanceOf(Empty.class)
        );
    }

    private Optional<Piece> findByPosition(List<Piece> pieces, Position position) {
        return pieces.stream()
                .filter(piece -> piece.findPosition(position))
                .findFirst();
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("체스 게임을 삭제할 수 없다.")
    void deleteBoardRunningGame() {
        final String title = "title";
        final String password = "password";
        Long id = chessService.createBoard(title, password);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(password)
                .when().delete("/api/boards/" + id)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400))
                .body("errorMessage", equalTo("게임이 진행중입니다."));

        assertThatCode(() -> chessService.loadGame(id))
                .doesNotThrowAnyException();
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("비밀번호가 틀리면 체스 게임을 삭제할 수 없다.")
    void deleteBoardWrongPassword() {
        final String title = "title";
        final String password = "password";
        Long id = chessService.createBoard(title, password);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body("wrongPassword")
                .when().delete("/api/boards/" + id)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body("statusCode", equalTo(400))
                .body("errorMessage", equalTo("비밀번호가 틀렸습니다."));

        assertThatCode(() -> chessService.loadGame(id))
                .doesNotThrowAnyException();
    }
}
