package study;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
public class CookieRegexTest {

    private static final String SET_COOKIE_REGEX = "player_validation=(.*); Max-Age=1000; Expires=(.*); Path=(.*)";
    private static final Pattern PATTERN = Pattern.compile(SET_COOKIE_REGEX);
    private static final String COOKIE_VALUE = "EKHY3FS92AxuKW41jvKg4Q==";
    private static final String EXPIRATION_TIME = "Sat, 30-Apr-2022 06:19:18 GMT";
    private static final String PATH = "/game/6";

    private static final String INPUT = "player_validation=" + COOKIE_VALUE + "; "
            + "Max-Age=1000; "+"Expires=" + EXPIRATION_TIME + "; " + "Path=" + PATH;

    @Test
    void 정규표현식_부합_여부_확인() {
        Matcher matcher = PATTERN.matcher(INPUT);

        boolean actual = matcher.matches();

        assertThat(actual).isTrue();
    }

    @Test
    void 그룹별_값_일치_여부_확인() {
        Matcher matcher = PATTERN.matcher(INPUT);
        matcher.matches();

        assertThat(matcher.group(1)).isEqualTo(COOKIE_VALUE);
        assertThat(matcher.group(2)).isEqualTo(EXPIRATION_TIME);
        assertThat(matcher.group(3)).isEqualTo(PATH);
    }
}
