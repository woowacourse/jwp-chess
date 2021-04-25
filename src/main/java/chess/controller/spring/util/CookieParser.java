package chess.controller.spring.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class CookieParser {
    public static String encodeCookie(String cookie) {
        if (cookie == null) {
            return null;
        }
        return new String(Base64.getUrlEncoder().withoutPadding()
            .encode(cookie.getBytes(StandardCharsets.UTF_8)));
    }

    public static String decodeCookie(String cookie) {
        if (cookie == null) {
            return null;
        }
        return new String(Base64.getUrlDecoder().decode(cookie));
    }
}
