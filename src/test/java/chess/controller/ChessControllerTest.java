package chess.controller;

import chess.TestConfig;
import chess.domain.GameStatus;
import chess.domain.Score;
import chess.domain.chesspiece.ChessPiece;
import chess.domain.chesspiece.Color;
import chess.domain.chesspiece.King;
import chess.domain.chesspiece.Knight;
import chess.domain.chesspiece.Queen;
import chess.domain.chesspiece.Rook;
import chess.domain.position.Position;
import chess.domain.result.EndResult;
import chess.dto.ChessPieceMapper;
import chess.dto.request.MoveRequestDto;
import chess.dto.request.RoomCreationRequestDto;
import chess.dto.request.RoomDeletionRequestDto;
import chess.dto.response.ChessPieceDto;
import chess.dto.response.CurrentTurnDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.hamcrest.core.Is;
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

    private final ObjectMapper objectMapper = new ObjectMapper();

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
    @DisplayName("게임을 시작한다.")
    void startGame() {
        // given
        final int roomId = roomDao.save("test", GameStatus.READY, Color.WHITE, "1234");

        // when

        // then
        RestAssured.given().log().all()
                .when().patch("/rooms/" + roomId + "/status")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());

    }

    @Test
    @DisplayName("모든 기물을 조회한다.")
    void findPieces() throws JsonProcessingException {
        // given
        final int roomId = roomDao.save("test", GameStatus.END, Color.WHITE, "1234");

        final Map<Position, ChessPiece> pieceByPosition = new HashMap<>();
        pieceByPosition.put(Position.from("a1"), King.from(Color.WHITE));
        pieceByPosition.put(Position.from("a2"), Queen.from(Color.WHITE));
        pieceByPosition.put(Position.from("a3"), Knight.from(Color.BLACK));
        chessPieceDao.saveAll(roomId, pieceByPosition);

        final List<ChessPieceDto> dtoList = pieceByPosition.entrySet()
                .stream()
                .map(it -> ChessPieceDto.of(
                        it.getKey(),
                        ChessPieceMapper.toPieceType(it.getValue()),
                        it.getValue().color()))
                .collect(Collectors.toList());

        // then
        RestAssured.given().log().all()
                .when().get("/rooms/" + roomId + "/pieces")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body(Is.is(objectMapper.writeValueAsString(dtoList)));
    }

    @Test
    @DisplayName("기물을 초기화한다.")
    void createPieces() {
        // given
        final int roomId = roomDao.save("test", GameStatus.PLAYING, Color.WHITE, "1234");

        // then
        RestAssured.given().log().all()
                .when().post("/rooms/" + roomId + "/pieces")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("기물을 이동 시킨다.")
    void movePiece() {
        // given
        final String from = "a1";
        final String to = "b2";

        final int roomId = roomDao.save("test", GameStatus.PLAYING, Color.WHITE, "1234");

        final Map<Position, ChessPiece> pieceByPosition = new HashMap<>();
        pieceByPosition.put(Position.from(from), King.from(Color.WHITE));
        chessPieceDao.saveAll(roomId, pieceByPosition);

        // when
        final MoveRequestDto requestDto = new MoveRequestDto(from, to);

        // then
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(requestDto)
                .when().patch("/rooms/" + roomId + "/pieces")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("현재 점수를 계산한다.")
    void findScore() throws JsonProcessingException {
        // given
        final int roomId = roomDao.save("test", GameStatus.PLAYING, Color.WHITE, "1234");

        final Map<Position, ChessPiece> pieceByPosition = new HashMap<>();
        pieceByPosition.put(Position.from("a1"), Queen.from(Color.WHITE));
        pieceByPosition.put(Position.from("a2"), Rook.from(Color.BLACK));
        chessPieceDao.saveAll(roomId, pieceByPosition);

        final Score score = new Score(pieceByPosition);

        // then
        RestAssured.given().log().all()
                .when().get("/rooms/" + roomId + "/scores")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body(Is.is(objectMapper.writeValueAsString(score)));
    }

    @Test
    @DisplayName("현재 턴을 조회한다.")
    void findTurn() throws JsonProcessingException {
        // given
        final String roomName = "test";
        final Color currentTurn = Color.BLACK;

        final int roomId = roomDao.save(roomName, GameStatus.PLAYING, currentTurn, "1234");

        final CurrentTurnDto response = CurrentTurnDto.of(roomName, currentTurn);

        // then
        RestAssured.given().log().all()
                .when().get("/rooms/" + roomId + "/turn")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body(Is.is(objectMapper.writeValueAsString(response)));
    }

    @Test
    @DisplayName("게임 결과를 계산한다.")
    void findResult() throws JsonProcessingException {
        // given
        final int roomId = roomDao.save("test", GameStatus.PLAYING, Color.BLACK, "1234");

        final Map<Position, ChessPiece> pieceByPosition = new HashMap<>();
        pieceByPosition.put(Position.from("a1"), Queen.from(Color.WHITE));
        pieceByPosition.put(Position.from("a2"), Rook.from(Color.BLACK));
        chessPieceDao.saveAll(roomId, pieceByPosition);

        final Score score = new Score(pieceByPosition);
        final EndResult endResult = new EndResult(score);

        // then
        RestAssured.given().log().all()
                .when().get("/rooms/" + roomId + "/result")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body(Is.is(objectMapper.writeValueAsString(endResult)));
    }
}
