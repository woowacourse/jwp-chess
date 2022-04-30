package chess.util;

import chess.domain.board.piece.Color;
import javax.servlet.http.Cookie;

public class CookieUtil {

    public static final String KEY = "player_validation";
    private static final int VALID_AGE = 1000;

    private CookieUtil() {
    }

    public static Cookie generateGameOwnerCookie(int gameId) {
        return generate(gameId, Color.WHITE);
    }

    public static Cookie generateOpponentCookie(int gameId) {
        return generate(gameId, Color.BLACK);
    }

    public static boolean validate(String cookieValue, int gameId, Color playerColor) {
        return cookieValue.equals(toEncrypted(gameId, playerColor));
    }

    private static Cookie generate(int gameId, Color playerColor) {
        Cookie cookie = new Cookie(KEY, toEncrypted(gameId, playerColor));
        cookie.setMaxAge(VALID_AGE);
        cookie.setPath("/game/" + gameId);
        return cookie;
    }

    private static String toEncrypted(int gameId, Color playerColor) {
        String value = gameId + playerColor.name();
        return HashUtil.hash(value);
    }
}