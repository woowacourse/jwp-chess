package chess.service;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Objects;

@Component
public class CookieHandler {

    private static final Integer DEFAULT_MAX_AGE = 60 * 5;
    private static final String DEFAULT_PATH = "/";
    private static final String DEFAULT_COOKIE_NAME = "web_chess_";

    public void addPlayerIdCookie(final HttpServletResponse response,
                                  final long roomId, final String playerId) {
        addCookie(response, DEFAULT_COOKIE_NAME + roomId, playerId);
    }

    private void addCookie(final HttpServletResponse response,
                           final String name, final String value) {
        final Cookie playerIdCookie = new Cookie(name, value);
        playerIdCookie.setMaxAge(DEFAULT_MAX_AGE);
        playerIdCookie.setPath(DEFAULT_PATH);
        response.addCookie(playerIdCookie);
    }

    public void extendAge(final Cookie cookie, final HttpServletResponse response) {
        cookie.setMaxAge(DEFAULT_MAX_AGE);
        response.addCookie(cookie);
    }

    public void remove(final long roomId, final HttpServletRequest request, final HttpServletResponse response) {
        final Cookie cookie = search(roomId, request);

        if (Objects.isNull(cookie)) {
            return;
        }

        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    public Cookie search(long roomId, final HttpServletRequest request) {
        if (Objects.isNull(request.getCookies())) {
            return null;
        }

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(DEFAULT_COOKIE_NAME + roomId))
                .findFirst()
                .orElse(null);
    }
}
