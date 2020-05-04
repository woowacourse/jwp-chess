package wooteco.chess.controller;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import wooteco.chess.dto.GameRequestDto;
import wooteco.chess.dto.GameResponseDto;
import wooteco.chess.dto.GameScoreDto;
import wooteco.chess.dto.MoveRequestDto;
import wooteco.chess.dto.PieceResponseDto;
import wooteco.chess.dto.RoomRequestDto;
import wooteco.chess.repository.entity.RoomEntity;
import wooteco.chess.service.SpringRoomService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpringGameControllerTest {
    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Autowired
    private SpringGameController springGameController;

    @Autowired
    private SpringRoomService springRoomService;

    private final RoomRequestDto roomRequestDto = new RoomRequestDto("hello", "world");

    @DisplayName("SpringRoomController가 빈으로 등록됐는지 확인")
    @Test
    void testSpringRoomControllerIsRegisteredBean() {
        assertThat(springGameController).isNotNull();
    }

    @DisplayName("init 했을 때 Response에 값이 비지 않았는지")
    @Test
    void testInit() {
        RoomEntity roomEntity = springRoomService.addRoom(roomRequestDto);
        GameRequestDto gameRequestDto = new GameRequestDto(roomEntity.getId());

        GameResponseDto gameResponseDto =
        given().
            contentType(ContentType.JSON).
            body(gameRequestDto).
            log().all().
        when().
            post("/game/init").
        then().
            log().all().
        and().
            statusCode(HttpStatus.SC_OK).
        and().
            contentType(ContentType.JSON).
        and().
            extract().
            as(GameResponseDto.class);

        assertThat(gameResponseDto.getPieces()).isNotEmpty();
    }

    @DisplayName("게임을 생성하고 난 뒤, 흑과 백의 score가 같은지 체크")
    @Test
    void testShowStatus() {
        RoomEntity roomEntity = springRoomService.addRoom(roomRequestDto);
        GameRequestDto gameRequestDto = new GameRequestDto(roomEntity.getId());

        GameScoreDto gameScoreDto =
            given().
                contentType(ContentType.JSON).
                body(gameRequestDto).
                log().all().
            when().
                post("/game/score").
            then().
                log().all().
            and().
                statusCode(HttpStatus.SC_OK).
            and().
                contentType(ContentType.JSON).
            and().
                extract().
                as(GameScoreDto.class);

        assertThat(gameScoreDto.getBlackScore()).isEqualTo(gameScoreDto.getWhiteScore());
    }

    @DisplayName("a2(폰)의 갈 수 있는 위치가 null이 아닌지 확인")
    @Test
    void testGetMovablePositions() {
        RoomEntity roomEntity = springRoomService.addRoom(roomRequestDto);
        MoveRequestDto moveRequestDto = new MoveRequestDto(roomEntity.getId(), "a2", null);

        List<String> movablePositions =
            given().
                contentType(ContentType.JSON).
                body(moveRequestDto).
                log().all().
            when().
                post("/game/path").
            then().
                log().all().
            and().
                statusCode(HttpStatus.SC_OK).
            and().
                contentType(ContentType.JSON).
            and().
                extract().
                as(ArrayList.class);

        assertThat(movablePositions).isNotNull();
    }

    @DisplayName("a2(폰)가 a4로 이동하고 그 결과가 맞는지 확인")
    @Test
    void testMove() {
        RoomEntity roomEntity = springRoomService.addRoom(roomRequestDto);
        MoveRequestDto moveRequestDto = new MoveRequestDto(roomEntity.getId(), "a2", "a4");

        GameResponseDto gameResponseDto =
            given().
                contentType(ContentType.JSON).
                body(moveRequestDto).
                log().all().
            when().
                post("/game/move").
            then().
                log().all().
            and().
                statusCode(HttpStatus.SC_OK).
            and().
                contentType(ContentType.JSON).
            and().
                extract().
                as(GameResponseDto.class);

        PieceResponseDto piece = gameResponseDto.getPieces().stream()
            .filter(pieceResponseDto -> "a4".equals(pieceResponseDto.getPosition()))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("제대로 이동하지 못 했습니다."));

        assertThat(piece).isNotNull();
    }

    @DisplayName("load했을 때 값이 Null이 아닌지")
    @Test
    void testLoad() {
        RoomEntity roomEntity = springRoomService.addRoom(roomRequestDto);
        GameRequestDto gameRequestDto = new GameRequestDto(roomEntity.getId());

        GameResponseDto gameResponseDto =
            given().
                contentType(ContentType.JSON).
                body(gameRequestDto).
                log().all().
            when().
                post("/game/load").
            then().
                log().all().
            and().
                statusCode(HttpStatus.SC_OK).
            and().
                contentType(ContentType.JSON).
            and().
                extract().
                as(GameResponseDto.class);

        assertThat(gameResponseDto.getPieces()).isNotNull();
    }



}
