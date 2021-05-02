package chess.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import chess.controller.spring.util.CookieParser;
import chess.domain.web.User;
import chess.repository.UserDao;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserDao userDao;

    @Test
    @DisplayName("유저 추가 - 기존 유저가 존재할 경우 테스트")
    void addUserWhenUserIsExist() {
        Optional<User> userExist = Optional.of(new User(1, "name"));
        given(userDao.findByName(eq("name"))).willReturn(userExist);

        int userId = userService.addUserIfNotExist("name");

        assertThat(userId).isEqualTo(1);
    }

    @Test
    @DisplayName("유저 추가 - 기존 유저가 없을 경우 테스트")
    void addUserWhenUserIsNotExist() {
        Optional<User> userNotExist = Optional.empty();
        given(userDao.findByName(any())).willReturn(userNotExist);
        given(userDao.addUser(any())).willReturn(2);

        int userId = userService.addUserIfNotExist("not_name");

        assertThat(userId).isEqualTo(2);
    }

    @Test
    @DisplayName("로그인 - cookie 와 일치하는 사용자가 있을 경우")
    void checkLoginWhenUserIsExist() {
        given(userDao.findUserById(anyInt())).willReturn(Optional.of(new User(1, "name")));

        Optional<Integer> userId = userService.checkLogin(CookieParser.encodeCookie("1"));

        assertThat(userId.isPresent()).isTrue();
        assertThat(userId).get().isEqualTo(1);
    }

    @Test
    @DisplayName("로그인 - cookie 가 null 일 경우")
    void checkLoginWhenCookieNull() {
        Optional<Integer> userId = userService.checkLogin(null);

        assertThat(userId).isEmpty();
    }

    @Test
    @DisplayName("로그인 - cookie 와 일치하는 사용자가 없는 경우")
    void checkLoginWhenUserIsNotExist() {
        given(userDao.findUserById(anyInt())).willReturn(Optional.empty());

        Optional<Integer> userId = userService.checkLogin(CookieParser.encodeCookie("2"));

        assertThat(userId).isEmpty();
    }
}