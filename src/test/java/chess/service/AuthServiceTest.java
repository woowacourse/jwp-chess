package chess.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import chess.domain.auth.AuthCredentials;
import chess.domain.auth.EncryptedAuthCredentials;
import chess.dto.response.EnterGameDto;
import chess.fixture.GameDaoStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@SuppressWarnings("NonAsciiCharacters")
class AuthServiceTest {

    private GameDaoStub gameDao;
    private AuthService service;

    @BeforeEach
    void setup() {
        gameDao = new GameDaoStub();
        service = new AuthService(gameDao);
    }

    @DisplayName("loginOrSignInAsOpponent 메서드로 게임의 정보 조회 가능")
    @Nested
    class LoginOrSignInAsOpponentTest {

        @Test
        void 방주인_비밀번호_입력시_방주인으로_로그인() {
            EnterGameDto actual = service.loginOrSignUpAsOpponent(
                    5, toEncryptedCredentials("참여자가_없는_게임", "encrypted5"));

            EnterGameDto expected = EnterGameDto.ofOwner(5);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 상대방이_등록되지_않은_경우_입력된_비밀번호로_등록시도() {
            EnterGameDto actual = service.loginOrSignUpAsOpponent(
                    5, toEncryptedCredentials("참여자가_없는_게임", "비밀번호!!"));

            EnterGameDto expected = EnterGameDto.ofOpponent(5);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 입력된_비밀번호가_유효하면_상대방_플레이어로_등록_성공() {
            service.loginOrSignUpAsOpponent(5, toEncryptedCredentials("참여자가_없는_게임", "비밀번호!!"));

            boolean actual = gameDao.findFullDataById(5).hasOpponent();

            assertThat(actual).isTrue();
        }

        @Test
        void 상대방이_등록된_경우_입력된_비밀번호로_로그인시도() {
            EnterGameDto actual = service.loginOrSignUpAsOpponent(
                    4, toEncryptedCredentials("참여자가_있는_게임", "enemy4"));

            EnterGameDto expected = EnterGameDto.ofOpponent(4);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 입력된_비밀번호가_두_플레이어의_비밀번호와_일치하지_않으면_예외발생() {
            assertThatThrownBy(() -> service.loginOrSignUpAsOpponent(
                    4, toEncryptedCredentials("참여자가_있는_게임", "wrong!")))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("잘못된 비밀번호를 입력하였습니다.");
        }
    }

    private EncryptedAuthCredentials toEncryptedCredentials(String name, String password) {
        return new AuthCredentials(name, password).toEncrypted();
    }
}
