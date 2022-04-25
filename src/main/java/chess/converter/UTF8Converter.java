package chess.converter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class UTF8Converter {

    private static final String UTF_8 = "UTF-8";

    public static String encode(String input) throws UnsupportedEncodingException {
        return URLEncoder.encode(input, UTF_8);
    }
}
