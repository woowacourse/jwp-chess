package chess.controller.spring;

import static org.assertj.core.api.Assertions.assertThat;
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
        final Long gameId = gameService.createGame(member1Id, member2Id);
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
        final Long gameId = gameService.createGame(member1Id, member2Id);

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
        final Long gameId = gameService.createGame(member1Id, member2Id);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().put("/games/" + gameId + "/terminate")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());

        final ChessGame game = gameService.findByGameId(gameId);
        assertThat(game.isEnd()).isTrue();
    }

    @DisplayName("멤버 id를 두 개 주면서 게임 생성을 요청하면 해당 멤버들이 참가하는 게임이 생성된다.")
    @Test
    void createGame() {
        final Long member1Id = memberService.addMember("알렉스");
        final Long member2Id = memberService.addMember("짱구");

        final CreateGameRequestDto createGameRequestDto = new CreateGameRequestDto(member1Id, member2Id);
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(createGameRequestDto)
                .when().post("/games")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }
}