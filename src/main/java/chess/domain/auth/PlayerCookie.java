package chess.domain.auth;

import javax.servlet.http.Cookie;

public class PlayerCookie {

    private final String value;

    protected PlayerCookie(String value) {
        this.value = value;
    }

    public static PlayerCookie of(Cookie cookie) {
        return new PlayerCookie(cookie.getValue());
    }

    @Override
    public String toString() {
        return "PlayerCookie{" + "value='" + value + '\'' + '}';
    }
}
