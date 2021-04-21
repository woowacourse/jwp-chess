package chess.service.spring;

import chess.domain.user.User;
import chess.repository.spring.UserDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class UserServiceTest {

    @InjectMocks
    public UserService userService;

    @Mock
    public UserDAO userDAO;

    @Mock
    public PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("유저를 정상적으로 추가한다.")
    @Test
    void addUser() {
        String password = "test";
        String encodedPassword = new BCryptPasswordEncoder().encode(password);
        given(userDAO.findByRoomId(1)).willReturn(Collections.emptyList());
        given(passwordEncoder.encode(password)).willReturn(encodedPassword);

        userService.addUser(password, 1);

        verify(userDAO, times(1)).findByRoomId(1);
        verify(passwordEncoder, times(1)).encode(password);
    }

    @DisplayName("한 방에 2명의 유저가 존재하면 유저를 추가할 수 없다.")
    @Test
    void cannotAddUser() {
        List<User> users = Arrays.asList(new User(1, "encoded", "BLACK", 1),
                new User(2, "enco", "WHITE", 1));
        given(userDAO.findByRoomId(1)).willReturn(users);

        assertThatCode(() -> userService.addUser("pass", 1))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 꽉 찬 방입니다.");
    }
}
