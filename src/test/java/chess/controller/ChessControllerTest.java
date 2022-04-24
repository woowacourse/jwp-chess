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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource("classpath:application-test.properties")
class ChessControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private GameDao gameDao;

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("drop table if exists member");
        jdbcTemplate.execute("drop table if exists piece");
        jdbcTemplate.execute("drop table if exists game");

        jdbcTemplate.execute("create table Member ("
                + "id bigint auto_increment primary key, "
                + "name varchar(10) not null);");

        jdbcTemplate.execute("create table Game ( "
                + "id bigint auto_increment primary key, "
                + "turn varchar(10) not null,"
                + "white_member_id bigint, "
                + "black_member_id bigint);");

        jdbcTemplate.execute("create table Piece ( "
                + "game_id bigint not null, "
                + "square_file varchar(2) not null, "
                + "square_rank varchar(2) not null, "
                + "team varchar(10), "
                + "piece_type varchar(10) not null, "
                + "constraint fk_game_id foreign key(game_id) references Game(id));");

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
                .statusCode(HttpStatus.OK.value())
                .body("statusCode", is(400));
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
