package chess.controller;

import static org.hamcrest.core.Is.is;

import chess.dao.GameDao;
import chess.dao.MemberDao;
import chess.domain.Board;
import chess.domain.BoardInitializer;
import chess.domain.ChessGame;
import chess.domain.Member;
import chess.domain.Participant;
import chess.domain.piece.detail.Team;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
@Sql("classpath:init.sql")
class ChessControllerTest {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private GameDao gameDao;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("페이지가 반환되는지 확인한다.")
    @Test
    void indexPage() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("명령을 입력해 말을 움직일 수 있는지 확인한다.")
    @Test
    void movePiece() {
        final Member one = new Member(1L, "one");
        final Member two = new Member(2L, "two");
        final Board board = new Board(BoardInitializer.create());
        final ChessGame game = new ChessGame(1L, board, Team.WHITE, new Participant(one, two));

        memberDao.save(one);
        memberDao.save(two);
        gameDao.save(game);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body("rawFrom=a2&rawTo=a4")
                .when().post("/move/" + 1L)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("말이 이동할 수 없을 경우 오류코드를 반환한다.")
    @Test
    void cannotMovePiece() {
        final Member one = new Member(1L, "one");
        final Member two = new Member(2L, "two");
        final Board board = new Board(BoardInitializer.create());
        final ChessGame game = new ChessGame(1L, board, Team.WHITE, new Participant(one, two));

        memberDao.save(one);
        memberDao.save(two);
        gameDao.save(game);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body("rawFrom=a2&rawTo=a5")
                .when().post("/move/" + 1L)
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("결과를 반환한다.")
    @Test
    void result() {
        final Member one = new Member(1L, "one");
        final Member two = new Member(2L, "two");
        final Board board = new Board(BoardInitializer.create());
        final ChessGame game = new ChessGame(1L, board, Team.WHITE, new Participant(one, two));

        memberDao.save(one);
        memberDao.save(two);
        gameDao.save(game);

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/result/" + 1L)
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
