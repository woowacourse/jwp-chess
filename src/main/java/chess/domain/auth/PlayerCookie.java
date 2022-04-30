package chess.domain.auth;

import chess.domain.board.piece.Color;
import chess.exception.InvalidAccessException;
import chess.exception.InvalidStatus;
import chess.util.CookieUtil;
import javax.servlet.http.Cookie;

public class PlayerCookie {

    private final String value;

    protected PlayerCookie(String value) {
        this.value = value;
    }

    public static PlayerCookie of(Cookie cookie) {
        return new PlayerCookie(cookie.getValue());
    }

    public Color parsePlayerColorBy(int id) {
        if (CookieUtil.validate(value, id, Color.WHITE)) {
            return Color.WHITE;
        }
        if (CookieUtil.validate(value, id, Color.BLACK)) {
            return Color.BLACK;
        }
        throw new InvalidAccessException(InvalidStatus.INVALID_COOKIE);
    }

    @Override
    public String toString() {
        return "PlayerCookie{" + "value='" + value + '\'' + '}';
    }
}
