package wooteco.chess;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import wooteco.chess.entity.RoomEntity;
import wooteco.chess.repository.RoomRepository;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChessControllerTest {
    @LocalServerPort
    int port;
    @Autowired
    private RoomRepository roomRepository;
    private RoomEntity roomEntity;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        roomEntity = roomRepository.save(new RoomEntity("우테코 체스"));
    }

    @DisplayName("새로운 게임 방을 생성해서 입장했을 때 알맞은 응답을 보내는지 확인")
    @Test
    void validateResponseToEnterRoom() {
        given().
                log().all().
                when().
                get("/chess/" + roomEntity.getId()).
                then().
                log().all().
                statusCode(200).
                body(containsString(":: 체스 게임 ::"));
    }

    @DisplayName("새 게임을 시작했을 때 알맞은 초기 화면 응답을 보내는지 확인")
    @Test
    void validateResponseToStartNewGame() {
        given().
                log().all().
                when().
                post("/chess/" + roomEntity.getId()).
                then().
                log().all().
                statusCode(200).
                body(containsString("38")).
                body(containsString("<div id=\"team\">White</div>"));
    }

    @DisplayName("White 말을 움직여서 Black 차례인 체스 게임을 불러왔을 때 알맞은 응답을 보내는지 확인")
    @Test
    void validateResponseToLoadGame() {
        given().
                log().all().
                when().
                post("/chess/" + roomEntity.getId());

        given().
                log().all().
                contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8").
                formParam("source", "a2").
                formParam("target", "a3").
                when().
                post("/chess/" + roomEntity.getId() + "/move");

        given().
                log().all().
                when().
                get("/chess/" + roomEntity.getId() + "/load").
                then().
                log().all().
                statusCode(200).
                body(containsString(":: 체스 게임 ::")).
                body(containsString("38")).
                body(containsString("<div id=\"team\">Black</div>"));
    }

    @DisplayName("체스말을 잘못 움직였을 때 알맞은 응답을 보내는지 확인")
    @ParameterizedTest
    @CsvSource(value = {"a1,a1,현재 자리한 위치(a1)로는 이동할 수 없습니다.", "a7,a5,위치(sourcePosition) a7의 말은 현재 차례인 White의 말이 아니므로 " +
            "움직일 " +
            "수 없습니다.", "2,a5,지정한 위치 a5는 하얀색 폰이 이동할 수 없는 곳입니다."})
    void validateResponseToMovePieceWrongly(String source, String target, String errorMsg) {
        given().
                log().all().
                when().
                post("/chess/" + roomEntity.getId());

        given().
                log().all().
                contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8").
                formParam("source", source).
                formParam("target", target).
                when().
                post("/chess/" + roomEntity.getId() + "/move").
                then().
                log().all().
                statusCode(200).
                body(containsString(errorMsg));
    }
}
