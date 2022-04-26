package chess.util;

import chess.domain.board.piece.Color;
import javax.servlet.http.Cookie;

public class CookieUtils {

    public static final String KEY = "player_validation";
    public static final int VALID_AGE = 1000;

    private CookieUtils() {
    }

    public static Cookie generate(int gameId, Color playerColor) {
        Cookie cookie = new Cookie(KEY, toEncrypted(gameId, playerColor));
        cookie.setMaxAge(VALID_AGE);
        return cookie;
    }

    public static boolean validate(String cookieValue, int gameId, Color playerColor) {
        return cookieValue.equals(toEncrypted(gameId, playerColor));
    }

    private static String toEncrypted(int gameId, Color playerColor) {
        String value = gameId + playerColor.name();
        return HashUtils.hash(value);
    }
}
