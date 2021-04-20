package chess.controller;

import chess.dto.MoveRequestDto;
import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.path.xml.config.XmlPathConfig;
import io.restassured.path.xml.element.Node;
import io.restassured.path.xml.element.NodeChildren;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("test")
@Sql("/sql/ddl.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class SpringChessControllerTest {
    private static final List<String> roomNames = Arrays.asList("포비방", "오즈방", "닉방");

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        for (String roomName : roomNames) {
            createRoom(roomName);
        }
    }

    private void createRoom(String roomName) {
        RestAssured.given().log().all()
                .queryParam("roomName", roomName)
                .when().post("/game")
                .then().log().all();
    }

    @DisplayName("메인 화면 제대로 반환 하는지")
    @Test
    void index() {
        RestAssured.given().log().all()
                .when().get("/")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.TEXT_HTML_VALUE)
                .body("html.body.h1", equalTo("메인 화면"));
    }

    @DisplayName("초기 체스방을 제대로 만들어서 체스방 view를 반환 해주는지")
    @Test
    void game() {
        String roomName = "삭정방";

        RestAssured.given().log().all()
                .queryParam("roomName", roomName)
                .when().post("/game")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.TEXT_HTML_VALUE)
                .body("html.body.div.h1", equalTo("ROOM " + roomName),
                        "html.body.div.find{it.@id == 'turn'}.label", equalTo("white의 차례입니다."));
    }

    @DisplayName("체스말을 움직이는 요청을 제대로 처리해서 이동시킨 view를 반환 해주는지")
    @Test
    void move() {
        String roomName = "포비방";
        List<String> command = Arrays.asList("move", "b2", "b4");

        MoveRequestDto moveRequestDto = new MoveRequestDto(roomName, String.join(" ", command));

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(moveRequestDto)
                .when().post("/game/move")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.TEXT_HTML_VALUE)
                .body("html.body.table.tr.find{it.@id == '" + command.get(1).charAt(1) + "'}.td.find{it.@id == '" + command.get(1) + "'}.img.@id", equalTo("blank_BLANK"),
                        "html.body.table.tr.find{it.@id == '" + command.get(2).charAt(1) + "'}.td.find{it.@id == '" + command.get(2) + "'}.img.@id", equalTo("white_PAWN"));
    }

    @DisplayName("저장되어 있는 방들의 목록 view를 제대로 반환해 주는지")
    @Test
    void rooms() {
        String dom = RestAssured.given().log().all()
                .when().get("/rooms")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.TEXT_HTML_VALUE)
                .extract().asString();

        XmlPath.config = XmlPathConfig.xmlPathConfig().allowDocTypeDeclaration(true);

        NodeChildren li = XmlPath.from(dom).getNodeChildren("html.body.div.form.ul.li");
        Iterator<Node> liNodes = li.nodeIterator();
        List<String> actualRoomNames = new ArrayList<>();
        while (liNodes.hasNext()) {
            Node node = liNodes.next();
            Node div = node.children()
                    .list()
                    .get(0);

            String roomName = div.children()
                    .list()
                    .get(0)
                    .getAttribute("value");

            actualRoomNames.add(roomName);
        }

        assertThat(actualRoomNames).hasSize(roomNames.size()).hasSameElementsAs(roomNames);
    }

    @DisplayName("요청한 방이름이 저장되어 있는 경우 방 상태를 제대로 반환 해주는지")
    @Test
    void load() {
        String roomName = "닉방";

        RestAssured.given().log().all()
                .queryParam("roomName", roomName)
                .when().post("/game/load")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.TEXT_HTML_VALUE)
                .body("html.body.div.h1", equalTo("ROOM " + roomName),
                        "html.body.div.find{it.@id == 'turn'}.label", equalTo("white의 차례입니다."));
    }

    @DisplayName("삭제 요청한 방이름이 저장되어 있는 경우 방 기록을 잘 삭제해 주는지")
    @Test
    void delete() {
        String roomName = "닉방";

        RestAssured.given().log().all()
                .queryParam("roomName", roomName)
                .when().delete("/delete/" + roomName)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON_VALUE);
    }

    @DisplayName("생성 요청한 방이름이 이미 저장되어 있는 경우 새로운 게임을 만들지 않고 경고 메세지와 함께 index 화면을 반환해주는지")
    @Test
    void gameHandleError() {
        String roomName = "오즈방";

        RestAssured.given().log().all()
                .queryParam("roomName", roomName)
                .when().post("/game")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value())
                .contentType(MediaType.TEXT_HTML_VALUE)
                .body("html.body.form.label.find{it.@class == 'alert'}", equalTo(roomName + "는 이미 존재하는 방입니다."));
    }
}