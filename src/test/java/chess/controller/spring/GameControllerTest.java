package chess.controller.spring;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.notNullValue;

import chess.domain.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.piece.detail.PieceType;
import chess.domain.square.Square;
import chess.dto.CreateGameRequestDto;
import chess.dto.MoveRequestDto;
import chess.service.GameService;
import chess.service.MemberService;
import io.restassured.RestAssured;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private GameService gameService;

    @Autowired
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("게임에 정상적인 기물 이동 요청이 오면 DB에 이동된 기물이 반영된다.")
    @Test
    void movePiece() {
        final Long member1Id = memberService.addMember("알렉스");
        final Long member2Id = memberService.addMember("짱구");
        final String title = "테스트 방";
        final String password = "1234";
        final Long gameId = gameService.createGame(title, password, member1Id, member2Id);
        final String rawTo = "a4";
        final MoveRequestDto moveRequestDto = new MoveRequestDto("a2", rawTo);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(moveRequestDto)
                .when().put("/games/" + gameId + "/move")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        final ChessGame game = gameService.findByGameId(gameId);
        final Square to = Square.from(rawTo);
        final Piece pawn = game.getBoard().getPieceAt(to);
        assertThat(pawn.getPieceType()).isSameAs(PieceType.PAWN);
    }

    @DisplayName("게임의 점수를 요청하면 blackScore와 whiteScore를 응답한다.")
    @Test
    void getGameScore() {
        final Long member1Id = memberService.addMember("알렉스");
        final Long member2Id = memberService.addMember("짱구");
        final String title = "테스트 방";
        final String password = "1234";
        final Long gameId = gameService.createGame(title, password, member1Id, member2Id);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/games/" + gameId + "/score")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("blackScore", notNullValue())
                .body("whiteScore", notNullValue());
    }

    @DisplayName("게임에 강제종료 요청을 하면 해당 게임이 종료된다.")
    @Test
    void terminateGame() {
        final Long member1Id = memberService.addMember("알렉스");
        final Long member2Id = memberService.addMember("짱구");
        final String title = "테스트 방";
        final String password = "1234";
        final Long gameId = gameService.createGame(title, password, member1Id, member2Id);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/games/" + gameId + "/terminate")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        final ChessGame game = gameService.findByGameId(gameId);
        assertThat(game.isEnd()).isTrue();
    }

    @DisplayName("멤버 id와 두 개와 방제목 및 비밀번호를 주면서 게임 생성을 요청하면 해당 멤버들이 참가하는 게임이 생성된다.")
    @Test
    void createGame() {
        final Long member1Id = memberService.addMember("알렉스");
        final Long member2Id = memberService.addMember("짱구");
        final String title = "테스트 방";
        final String password = "1234";

        final CreateGameRequestDto createGameRequestDto = new CreateGameRequestDto(title, password, member1Id,
                member2Id);
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createGameRequestDto)
                .when().post("/games")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("같은 멤버 id로 게임 생성을 요청하면 400을 반환한다.")
    @Test
    void createGameSameMember() {
        final Long member1Id = memberService.addMember("알렉스");
        final String title = "테스트 방";
        final String password = "1234";

        final CreateGameRequestDto createGameRequestDto = new CreateGameRequestDto(title, password, member1Id,
                member1Id);
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createGameRequestDto)
                .when().post("/games")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("종료되지 않은 게임에 삭제요청하면 400 상태코드를 반환한다.")
    @Test
    void deletePlayingGame() {
        final Long member1Id = memberService.addMember("알렉스");
        final Long member2Id = memberService.addMember("짱구");
        final String title = "테스트 방";
        final String password = "1234";

        final Long gameId = gameService.createGame(title, password, member1Id, member2Id);

        final CreateGameRequestDto createGameRequestDto = new CreateGameRequestDto(title, password, member1Id,
                member2Id);
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createGameRequestDto)
                .when().delete("/games/" + gameId)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("종료된 게임에 삭제요청하면 게임이 삭제된다.")
    @Test
    void deleteEndGame() {
        final Long member1Id = memberService.addMember("알렉스");
        final Long member2Id = memberService.addMember("짱구");
        final String title = "테스트 방";
        final String password = "1234";

        final Long gameId = gameService.createGame(title, password, member1Id, member2Id);

        gameService.terminate(gameId);

        final CreateGameRequestDto createGameRequestDto = new CreateGameRequestDto(title, password, member1Id,
                member2Id);
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createGameRequestDto)
                .when().delete("/games/" + gameId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        assertThatThrownBy(() -> gameService.findByGameId(gameId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("찾는 게임이 존재하지 않습니다.");
    }
}