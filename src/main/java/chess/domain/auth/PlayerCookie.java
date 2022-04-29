package chess.domain.auth;

import chess.domain.board.piece.Color;
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
        throw new IllegalArgumentException("해당 게임의 플레이어가 아닙니다.");
    }

    @Override
    public String toString() {
        return "PlayerCookie{" + "value='" + value + '\'' + '}';
    }
}
