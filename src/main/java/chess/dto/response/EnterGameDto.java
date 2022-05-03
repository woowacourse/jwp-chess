package chess.dto.response;

import chess.util.CookieUtil;
import java.util.Objects;
import javax.servlet.http.Cookie;

public class EnterGameDto {

    private static final String VALID_WHITE_PLAYER_MESSAGE = "백색 진영으로 참가하였습니다.";
    private static final String VALID_BLACK_PLAYER_MESSAGE = "흑색 진영으로 참가하였습니다.";

    private final Cookie cookie;
    private final String message;

    private EnterGameDto(Cookie cookie, String message) {
        this.cookie = cookie;
        this.message = message;
    }

    public static EnterGameDto ofOwner(int gameId) {
        Cookie cookie = CookieUtil.generateGameOwnerCookie(gameId);
        return new EnterGameDto(cookie, VALID_WHITE_PLAYER_MESSAGE);
    }

    public static EnterGameDto ofOpponent(int gameId) {
        Cookie cookie = CookieUtil.generateOpponentCookie(gameId);
        return new EnterGameDto(cookie, VALID_BLACK_PLAYER_MESSAGE);
    }

    public Cookie getCookie() {
        return cookie;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EnterGameDto that = (EnterGameDto) o;
        return Objects.equals(cookie.getValue(), that.cookie.getValue())
                && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cookie.getValue(), message);
    }

    @Override
    public String toString() {
        return "EnterGameDto{" +
                "cookie=" + cookie +
                ", message='" + message + '\'' +
                '}';
    }
}
