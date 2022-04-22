package chess.controller.spring;

import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

import chess.domain.ChessGame;
import chess.domain.piece.Piece;
import chess.domain.piece.detail.PieceType;
import chess.domain.square.Square;
import chess.dto.CreateGameRequestDto;
import chess.dto.MoveRequestDto;
import chess.service.GameService;
import chess.service.MemberService;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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