package chess.util;

//import chess.domain.board.piece.Color;
//import javax.servlet.http.Cookie;

// TODO: 추가 미션 요구사항 관련.
public class CookieUtils {

//    public static final String KEY = "player_validation";
//    private static final int VALID_AGE = 1000;
//
//    private CookieUtils() {
//    }
//
//    public static Cookie generate(int gameId, Color playerColor) {
//        Cookie cookie = new Cookie(KEY, toEncrypted(gameId, playerColor));
//        cookie.setMaxAge(VALID_AGE);
//        cookie.setPath("/game/" + gameId);
//        return cookie;
//    }
//
//    public static boolean validate(String cookieValue, int gameId, Color playerColor) {
//        return cookieValue.equals(toEncrypted(gameId, playerColor));
//    }
//
//    private static String toEncrypted(int gameId, Color playerColor) {
//        String value = gameId + playerColor.name();
//        return HashUtils.hash(value);
//    }
}
