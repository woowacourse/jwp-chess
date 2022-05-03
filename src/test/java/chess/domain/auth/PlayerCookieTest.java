package chess.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.board.piece.Color;
import chess.exception.InvalidAccessException;
import chess.util.CookieUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class PlayerCookieTest {

    private static final int VALID_GAME_ID = 1;
    private static final int INVALID_GAME_ID = 9999;

    private final PlayerCookie whiteCookie = PlayerCookie.of(CookieUtil.generateGameOwnerCookie(VALID_GAME_ID));
    private final PlayerCookie blackCookie = PlayerCookie.of(CookieUtil.generateOpponentCookie(VALID_GAME_ID));

    @DisplayName("parsePlayerColorBy 메서드는 쿠키값에서 플레이어의 진영 정보를 추출한다")
    @Nested
    class ParsePlayerColorByTest {

        @DisplayName("게임의 id값과 플레이어의 진영이 백색으로 일치하면 참 반환")
        @Test
        void whiteCookieMatch() {
            Color actual = whiteCookie.parsePlayerColorBy(VALID_GAME_ID);

            assertThat(actual).isEqualTo(Color.WHITE);
        }

        @DisplayName("게임의 id값과 플레이어의 진영이 흑색으로 일치하면 참 반환")
        @Test
        void blackCookieMatch() {
            Color actual = blackCookie.parsePlayerColorBy(VALID_GAME_ID);

            assertThat(actual).isEqualTo(Color.BLACK);
        }

        @Test
        void 게임의_id값이_잘못된_경우_예외발생() {
            assertThatThrownBy(() -> whiteCookie.parsePlayerColorBy(INVALID_GAME_ID))
                    .isInstanceOf(InvalidAccessException.class)
                    .hasMessage("해당 게임의 플레이어가 아닙니다.");
        }
    }

    @Test
    void nullCookie는_parsePlayerColorBy_메서드_호출시_예외발생() {
        PlayerCookie cookie = new NullCookie();
        assertThatThrownBy(() -> cookie.parsePlayerColorBy(INVALID_GAME_ID))
                .isInstanceOf(InvalidAccessException.class)
                .hasMessage("로그인이 필요합니다.");
    }
}