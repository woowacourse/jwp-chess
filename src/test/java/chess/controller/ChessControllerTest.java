package chess.controller;

import chess.TestConfig;
import chess.domain.ChessGame;
import chess.domain.GameStatus;
import chess.domain.Score;
import chess.domain.chessboard.ChessBoard;
import chess.domain.chessboard.ChessBoardFactory;
import chess.domain.chesspiece.ChessPiece;
import chess.domain.chesspiece.Color;
import chess.domain.chesspiece.King;
import chess.domain.chesspiece.Knight;
import chess.domain.chesspiece.Queen;
import chess.domain.chesspiece.Rook;
import chess.domain.position.Position;
import chess.domain.result.EndResult;
import chess.domain.room.Room;
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
import util.FakeChessGameRepository;
import util.FakeRoomRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import(TestConfig.class)
class ChessControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @LocalServerPort
    int port;

    @Autowired
    private FakeRoomRepository roomRepository;

    @Autowired
    private FakeChessGameRepository chessGameRepository;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @AfterEach
    void clear() {
        roomRepository.deleteAll();
        chessGameRepository.deleteAll();
    }

    @Test
    @DisplayName("모든 방을 조회한다.")
    void findAllRoom() {
        roomRepository.add(new Room(
                "test1",
                "1234",
                new ChessGame(ChessBoardFactory.createChessBoard(), GameStatus.READY)));
        roomRepository.add(new Room(
                "test2",
                "1234",
                new ChessGame(ChessBoardFactory.createChessBoard(), GameStatus.PLAYING)));
        roomRepository.add(new Room(
                "test3",
                "1234",
                new ChessGame(ChessBoardFactory.createChessBoard(), GameStatus.END)));
        roomRepository.add(new Room(
                "test4",
                "1234",
                new ChessGame(ChessBoardFactory.createChessBoard(), GameStatus.KING_DIE)));

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

        final int roomId = roomRepository.add(new Room(
                roomName,
                plainPassword,
                new ChessGame(ChessBoardFactory.createChessBoard(), GameStatus.END)));
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
        final int roomId = roomRepository.add(new Room(
                "test",
                "1234",
                new ChessGame(ChessBoardFactory.createChessBoard(), GameStatus.READY)));

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
        final Map<Position, ChessPiece> pieceByPosition = new HashMap<>();
        pieceByPosition.put(Position.from("a1"), King.from(Color.WHITE));
        pieceByPosition.put(Position.from("a2"), Queen.from(Color.WHITE));
        pieceByPosition.put(Position.from("a3"), Knight.from(Color.BLACK));

        final ChessBoard chessBoard = new ChessBoard(pieceByPosition);
        final ChessGame chessGame = new ChessGame(chessBoard, GameStatus.END);

        final Room room = new Room("test", "1234", chessGame);
        final int roomId = roomRepository.add(room);
        chessGameRepository.add(roomId, chessGame);

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
    @DisplayName("기물을 이동 시킨다.")
    void movePiece() {
        // given
        final String from = "a1";
        final String to = "b2";

        final Map<Position, ChessPiece> pieceByPosition = new HashMap<>();
        pieceByPosition.put(Position.from(from), King.from(Color.WHITE));
        pieceByPosition.put(Position.from("c2"), King.from(Color.BLACK));

        final ChessBoard chessBoard = new ChessBoard(pieceByPosition);
        final ChessGame chessGame = new ChessGame(chessBoard, GameStatus.PLAYING);

        final int roomId = roomRepository.add(new Room("test", "1234", chessGame));
        chessGameRepository.add(roomId, chessGame);

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
        final Map<Position, ChessPiece> pieceByPosition = new HashMap<>();
        pieceByPosition.put(Position.from("a1"), Queen.from(Color.WHITE));
        pieceByPosition.put(Position.from("a2"), Rook.from(Color.BLACK));

        final ChessBoard chessBoard = new ChessBoard(pieceByPosition);
        final ChessGame chessGame = new ChessGame(chessBoard, GameStatus.PLAYING);

        final int roomId = roomRepository.add(new Room("test", "1234", chessGame));
        chessGameRepository.add(roomId, chessGame);

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

        final ChessGame chessGame = new ChessGame(new ChessBoard(new HashMap<>(), currentTurn), GameStatus.PLAYING);

        final int roomId = roomRepository.add(new Room(roomName, "1234", chessGame));
        chessGameRepository.add(roomId, chessGame);

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
        final Map<Position, ChessPiece> pieceByPosition = new HashMap<>();
        pieceByPosition.put(Position.from("a1"), Queen.from(Color.WHITE));
        pieceByPosition.put(Position.from("a2"), Rook.from(Color.BLACK));

        final ChessBoard chessBoard = new ChessBoard(pieceByPosition, Color.BLACK);
        final ChessGame chessGame = new ChessGame(chessBoard, GameStatus.PLAYING);

        final int roomId = roomRepository.add(new Room("test", "1234", chessGame));

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
