package chess.controller;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;

import chess.RoomEntityFixtures;
import chess.controller.dto.ChessPieceMoveRequest;
import chess.controller.dto.RoomDeleteRequest;
import chess.controller.dto.RoomSaveRequest;
import chess.dao.JdbcChessPieceDao;
import chess.dao.JdbcRoomDao;
import chess.entity.RoomEntity;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ChessApiControllerTest {

    @LocalServerPort
    private int port;

    private final JdbcRoomDao roomDao;
    private final JdbcChessPieceDao chessPieceDao;

    @Autowired
    public ChessApiControllerTest(final JdbcRoomDao roomDao, final JdbcChessPieceDao chessPieceDao) {
        this.roomDao = roomDao;
        this.chessPieceDao = chessPieceDao;
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @DisplayName("GET /api/rooms")
    @Test
    void room_전체_조회한다() {
        roomDao.save(RoomEntityFixtures.generateRoomEntity());
        roomDao.save(RoomEntityFixtures.generateRoomEntity());
        roomDao.save(RoomEntityFixtures.generateRoomEntity());

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/rooms")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(3));
    }

    @DisplayName("GET /api/rooms/{id}")
    @Test
    void room_단건_조회한다() {
        final int id = roomDao.save(RoomEntityFixtures.generateRoomEntity());

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/rooms/" + id)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(id));
    }

    @DisplayName("GET /api/rooms/{id} - 404")
    @Test
    void room_단건_조회_실패한다() {
        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/rooms/" + 0)
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("POST /api/rooms")
    @Test
    void room_생성한다() {
        final String name = "매트의 체스";
        final String password = "123123";
        final RoomSaveRequest roomSaveRequest = new RoomSaveRequest(name, password);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(roomSaveRequest)
                .when().post("/api/rooms")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", containsString("/api/rooms/"));
    }

    @DisplayName("DELETE /api/rooms/{id}")
    @Test
    void room_삭제한다() {
        final int id = roomDao.save(RoomEntityFixtures.generateRoomEntity());
        final RoomEntity roomEntity = roomDao.findById(id).get();

        final RoomDeleteRequest roomDeleteRequest = new RoomDeleteRequest(roomEntity.getPassword());

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(roomDeleteRequest)
                .when().delete("/api/rooms/" + id)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("POST /api/rooms/{id}/pieces")
    @Test
    void pieces_생성한다() {
        final int id = roomDao.save(RoomEntityFixtures.generateRoomEntity());

        RestAssured.given().log().all()
                .when().post("/api/rooms/" + id + "/pieces")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", is("/api/rooms/" + id + "/pieces"));
    }

    @DisplayName("GET /api/rooms/{id}/pieces")
    @Test
    void pieces_조회한다() {
        final int id = roomDao.save(RoomEntityFixtures.generateRoomEntity());

        RestAssured.given()
                .when().post("/api/rooms/" + id + "/pieces")
                .then();

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/rooms/" + id + "/pieces")
                .then().log().all()
                .body("size()", is(32));
    }

    @DisplayName("PATCH /api/rooms/{id}/pieces")
    @Test
    void pieces_이동한다() {
        final int id = roomDao.save(RoomEntityFixtures.generateRoomEntity());

        RestAssured.given()
                .when().post("/api/rooms/" + id + "/pieces")
                .then();

        final ChessPieceMoveRequest chessPieceMoveRequest = new ChessPieceMoveRequest("a2", "a4");

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(chessPieceMoveRequest)
                .when().patch("/api/rooms/" + id + "/pieces")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @DisplayName("GET /api/rooms/{id}/scores")
    @Test
    void room_점수_조회한다() {
        final int id = roomDao.save(RoomEntityFixtures.generateRoomEntity());

        RestAssured.given()
                .when().post("/api/rooms/" + id + "/pieces")
                .then();

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/rooms/" + id + "/scores")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("whiteScore", is(38.0F))
                .body("blackScore", is(38.0F));
    }

    @DisplayName("GET /api/rooms/{id}/result")
    @Test
    void room_결과_조회한다() {
        final int id = roomDao.save(RoomEntityFixtures.generateRoomEntity());

        RestAssured.given()
                .when().post("/api/rooms/" + id + "/pieces")
                .then();

        RestAssured.given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/rooms/" + id + "/result")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("score.whiteScore", is(38.0F))
                .body("score.blackScore", is(38.0F));
    }

    @AfterEach
    void cleanUp() {
        roomDao.deleteAll();
    }
}
