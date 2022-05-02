package chess.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;

import chess.board.Board;
import chess.board.Turn;
import chess.board.piece.Empty;
import chess.board.piece.Pawn;
import chess.board.piece.Piece;
import chess.board.piece.Pieces;
import chess.board.piece.position.Position;
import chess.web.service.ChessService;
import chess.web.service.dto.BoardResponseDto;
import chess.web.service.dto.CreateBoardDto;
import chess.web.service.dto.MoveDto;
import chess.web.service.dto.ScoreDto;
import io.restassured.RestAssured;
import java.util.List;
import java.util.NoSuchElementException;
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
class ChessApiControllerTest {

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
    @DisplayName("정상적으로 체스방과 보드, 기물이 생성된다.")
    void createBoard() {
        final String title = "title";
        final String password = "password";
        CreateBoardDto createBoardDto = new CreateBoardDto(title, password);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createBoardDto)
                .when().post("/api/boards")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/chess/1");
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("체스 게임이 제대로 초기화된다.")
    void initBoard() {
        final String title = "title";
        final String password = "password";
        Long id = chessService.createBoard(title, password);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/api/boards/" + id + "/initialization")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("turn", equalTo("white"))
                .body("board.size()", is(64))
                .body("finish", equalTo(false));

        List<Piece> initPieces = Pieces.createInit().getPieces();
        List<Piece> savedPieces = chessService.loadGame(id).getPieces().getPieces();
        assertThat(initPieces.containsAll(savedPieces)).isTrue();
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("시작점과 도착점을 입력하면 정상적으로 기물이 이동한다.")
    void move() {
        final String title = "title";
        final String password = "password";
        Long id = chessService.createBoard(title, password);
        String fromPosition = "a2";
        String toPosition = "a4";
        MoveDto moveDto = new MoveDto(fromPosition, toPosition);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(moveDto)
                .when().patch("/api/boards/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("turn", equalTo("black"))
                .body("board.size()", is(64))
                .body("finish", equalTo(false));

        List<Piece> savedPieces = chessService.loadGame(id).getPieces().getPieces();
        Optional<Piece> pieceByFromPosition = findByPosition(savedPieces, Position.from(fromPosition));
        Optional<Piece> pieceByToPosition = findByPosition(savedPieces, Position.from(toPosition));
        assertAll(
                () -> assertThat(pieceByFromPosition).isPresent(),
                () -> assertThat(pieceByToPosition).isPresent(),
                () -> assertThat(pieceByFromPosition.get()).isInstanceOf(Empty.class),
                () -> assertThat(pieceByToPosition.get()).isInstanceOf(Pawn.class)
        );
    }

    private Optional<Piece> findByPosition(List<Piece> pieces, Position position) {
        return pieces.stream()
                .filter(piece -> piece.findPosition(position))
                .findFirst();
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("저장된 체스 게임의 상태들을 반환한다.")
    void loadGame() {
        final String title = "title";
        final String password = "password";
        Long id = chessService.createBoard(title, password);
        BoardResponseDto boardResponseDto = BoardResponseDto.from(Board.create(Pieces.createInit(), Turn.init()));

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/boards/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("turn", equalTo("white"))
                .body("board", equalTo(boardResponseDto.getBoard()))
                .body("finish", equalTo(false));

        List<Piece> initPieces = Pieces.createInit().getPieces();
        List<Piece> savedPieces = chessService.loadGame(id).getPieces().getPieces();
        assertThat(initPieces.containsAll(savedPieces)).isTrue();
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("현재 체스 기물 상태에 따라 블랙, 화이트 팀의 점수를 반환한다.")
    void getStatus() {
        final String title = "title";
        final String password = "password";
        Long id = chessService.createBoard(title, password);
        ScoreDto scoreDto = chessService.getStatus(id);
        float blackTeamScore = (float) scoreDto.getBlackTeamScore();
        float whiteTeamScore = (float) scoreDto.getWhiteTeamScore();

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/boards/" + id + "/status")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("blackTeamScore", equalTo(blackTeamScore))
                .body("whiteTeamScore", equalTo(whiteTeamScore));

        assertAll(
                () -> assertThat(scoreDto.getBlackTeamScore()).isEqualTo(38D),
                () -> assertThat(scoreDto.getWhiteTeamScore()).isEqualTo(38D)
        );
    }

    @Sql("/sql/chess-setup.sql")
    @Test
    @DisplayName("체스 게임을 삭제한다.")
    void deleteBoard() {
        final String title = "title";
        final String password = "password";
        Long id = chessService.createBoard(title, password);
        chessService.move(new MoveDto("e2", "e3"), id);
        chessService.move(new MoveDto("f7", "f6"), id);
        chessService.move(new MoveDto("d1", "h5"), id);
        chessService.move(new MoveDto("a7", "a6"), id);
        chessService.move(new MoveDto("h5", "e8"), id);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(password)
                .when().delete("/api/boards/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body(equalTo("true"));

        assertThatThrownBy(() -> chessService.loadGame(id))
                .isInstanceOf(NoSuchElementException.class);
    }
}
