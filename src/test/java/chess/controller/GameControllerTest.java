package chess.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;

import chess.domain.auth.AuthCredentials;
import chess.util.CookieUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GameControllerTest {

    private static final String SET_COOKIE_REGEX = "player_validation=(.*); Max-Age=1000; Expires=(.*); Path=(.*)";
    private static final Pattern SET_COOKIE_HEADER_PATTERN = Pattern.compile(SET_COOKIE_REGEX);

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    void createGame() {
        AuthCredentials clientInput = new AuthCredentials("방이름", "비밀번호");

        int expectedNewGameId = 5 + 1;
        Cookie expectedCookie = CookieUtil.generateGameOwnerCookie(expectedNewGameId);

        Response response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(clientInput)
                .when().post("/game");

        response.then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/game/" + expectedNewGameId)
                .body(is(expectedNewGameId + ""));

        Matcher matcher = getMatcher(response.getHeader("Set-Cookie"));
        assertThat(matcher.group(1)).isEqualTo(expectedCookie.getValue());
        assertThat(matcher.group(3)).isEqualTo(expectedCookie.getPath());
    }

    private Matcher getMatcher(String setCookieHeaderValue) {
        Matcher matcher = SET_COOKIE_HEADER_PATTERN.matcher(setCookieHeaderValue);
        matcher.matches();
        return matcher;
    }
}
