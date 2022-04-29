package chess.util;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.board.piece.Color;
import javax.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
public class CookieUtilTest {

    @DisplayName("생성되는 쿠키값은 gameId와 진영 정보에 따라 정해진다")
    @Nested
    class CookieValueTest {

        @Test
        void generateGameOwnerCookie_메서드는_gameId와_백색_문자열을_토대로_쿠키값을_생성() {
            Cookie actual = CookieUtil.generateGameOwnerCookie(1);

            Cookie expected = generateCookie(1, Color.WHITE);

            assertThat(actual.getValue()).isEqualTo(expected.getValue());
        }

        @Test
        void generateOpponentCookie_메서드는_gameId와_흑색_문자열을_토대로_쿠키값을_생성() {
            Cookie actual = CookieUtil.generateOpponentCookie(1);

            Cookie expected = generateCookie(1, Color.BLACK);

            assertThat(actual.getValue()).isEqualTo(expected.getValue());
        }

        @Test
        void 동일한_gameId여도_진영에_따라_다른_쿠키값_생성() {
            Cookie cookie1 = CookieUtil.generateGameOwnerCookie(1);
            Cookie cookie2 = CookieUtil.generateOpponentCookie(1);

            assertThat(cookie1.getValue()).isNotEqualTo(cookie2.getValue());
        }

        @Test
        void 동일한_진영이어도_gameId에_따라_다른_쿠키값_생성() {
            Cookie cookie1 = CookieUtil.generateGameOwnerCookie(1);
            Cookie cookie2 = CookieUtil.generateGameOwnerCookie(2);

            assertThat(cookie1.getValue()).isNotEqualTo(cookie2.getValue());
        }
    }

    @DisplayName("쿠키에 부가되는 정보에 대한 테스트")
    @Nested
    class CookieMetaDataTest {

        @Test
        void 생성되는_모든_쿠키의_최대수명은_1000초() {
            Cookie cookie1 = CookieUtil.generateGameOwnerCookie(1);
            Cookie cookie2 = CookieUtil.generateOpponentCookie(999);

            int actual1 = cookie1.getMaxAge();
            int actual2 = cookie2.getMaxAge();

            assertThat(actual1).isEqualTo(1000);
            assertThat(actual2).isEqualTo(1000);
        }

        @DisplayName("생성되는 쿠키의 경로는 /game/:gameId 형식")
        @Test
        void cookiePath() {
            Cookie cookie1 = CookieUtil.generateGameOwnerCookie(10);
            Cookie cookie2 = CookieUtil.generateOpponentCookie(10);

            String actual1 = cookie1.getPath();
            String actual2 = cookie1.getPath();

            assertThat(actual1).isEqualTo("/game/" + 10);
            assertThat(actual2).isEqualTo("/game/" + 10);
        }
    }

    @DisplayName("validate 메서드는 쿠키값이 특정 gameId와 진영에 부합하는지를 확인")
    @Nested
    class ValidateTest {

        @Test
        void 쿠키값이_특정_gameId와_진영에_부합하면_참을_반환() {
            Cookie cookie = generateCookie(1, Color.WHITE);
            String cookieValue = cookie.getValue();

            boolean actual = CookieUtil.validate(cookieValue, 1, Color.WHITE);

            assertThat(actual).isTrue();
        }

        @Test
        void 쿠키값이_다른_gameId로부터_생성되었으면_거짓을_반환() {
            Cookie cookie = generateCookie(9999, Color.WHITE);
            String cookieValue = cookie.getValue();

            boolean actual = CookieUtil.validate(cookieValue, 1, Color.WHITE);

            assertThat(actual).isFalse();
        }

        @Test
        void 쿠키값이_다른_진영으로부터_생성되었으면_거짓을_반환() {
            Cookie cookie = generateCookie(1, Color.BLACK);
            String cookieValue = cookie.getValue();

            boolean actual = CookieUtil.validate(cookieValue, 1, Color.WHITE);

            assertThat(actual).isFalse();
        }
    }

    private Cookie generateCookie(int gameId, Color playerColor) {
        return new Cookie(CookieUtil.KEY, HashUtils.hash(gameId + playerColor.name()));
    }
}
