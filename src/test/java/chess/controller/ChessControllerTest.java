package chess.controller;

import chess.TestConfig;
import chess.domain.GameStatus;
import chess.domain.chesspiece.Color;
import chess.dto.request.RoomCreationRequestDto;
import chess.dto.request.RoomDeletionRequestDto;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCrypt;
import util.FakeChessPieceDao;
import util.FakeRoomDao;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(TestConfig.class)
class ChessControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private FakeRoomDao roomDao;

    @Autowired
    private FakeChessPieceDao chessPieceDao;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void clear() {
        roomDao.deleteAll();
        chessPieceDao.deleteAll();
    }

    @Test
    @DisplayName("모든 방을 조회한다.")
    void findAllRoom() {
        roomDao.save("test1", GameStatus.READY, Color.WHITE, "1234");
        roomDao.save("test2", GameStatus.PLAYING, Color.WHITE, "1234");
        roomDao.save("test3", GameStatus.END, Color.WHITE, "1234");
        roomDao.save("test4", GameStatus.KING_DIE, Color.WHITE, "1234");

        RestAssured.given().log().all()
                .when().get("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    @DisplayName("방을 생성한다.")
    void createRoom() {
        // given
        final RoomCreationRequestDto requestDto = new RoomCreationRequestDto("test", "1234");

        // then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .when().post("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/rooms/1");
    }

    @Test
    @DisplayName("방을 삭제한다.")
    void deleteRoom() {
        // given
        final String roomName = "test";
        final String plainPassword = "1234";
        final String hashPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        final int roomId = roomDao.save(roomName, GameStatus.END, Color.WHITE, hashPassword);
        final RoomDeletionRequestDto requestDto = new RoomDeletionRequestDto(roomId, plainPassword);

        // then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .when().delete("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("기물을 초기화한다.")
    void createPieces() {
        // given
        final int roomId = roomDao.save("test", GameStatus.READY, Color.WHITE, "1234");

        // then
        RestAssured.given().log().all()
                .when().post("/rooms/" + roomId + "/pieces")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }
}