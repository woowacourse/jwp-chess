package chess.advice;

import chess.domain.ChessGame;
import chess.domain.Room;
import chess.domain.team.BlackTeam;
import chess.domain.team.WhiteTeam;
import chess.repository.ChessRepository;
import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Sql("classpath:tableInit.sql")
class ChessAdviceTest {
    private static final Room room = new Room(1, "멍토", "비번", 1);
    private static final ChessGame chessGame = new ChessGame(new WhiteTeam(), new BlackTeam());

    private final ChessRepository chessRepository;
    @LocalServerPort
    int port;

    public ChessAdviceTest(final ChessRepository chessRepository) {
        this.chessRepository = chessRepository;
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        chessRepository.createRoom(chessGame, room);
    }

    @Test
    @DisplayName("없는 데이터 사용 에러 테스트")
    void handleEmptyResultDataAccessException() {
        Room room = new Room(2, "멍토", "비번", 2);

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(room)
            .when().post("/api/rooms/2")
            .then().log().all()
            .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("로그인 패스워드 실패 테스트")
    void handleLoginFailException() {
        Room room = new Room(1, "멍토", "비번틀림", 1);

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(room)
            .when().post("/api/rooms/1")
            .then().log().all()
            .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    @DisplayName("잘못된 이동 요청 테스트")
    void handleIllegalArgumentException() {
        Map<String, String> body = new HashMap<>();
        body.put("to", "a6");

        RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .when().put("/api/rooms/1/pieces/a2")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}