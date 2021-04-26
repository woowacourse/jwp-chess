package chess.service.spring;

import chess.domain.piece.TeamType;
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
        given(userDAO.findAllByRoomId(1)).willReturn(Collections.emptyList());
        given(passwordEncoder.encode(password)).willReturn(encodedPassword);

        userService.addUserIntoRoom(1, password);

        verify(userDAO, times(1)).findAllByRoomId(1);
        verify(passwordEncoder, times(1)).encode(password);
    }

    @DisplayName("한 방에 2명의 유저가 존재하면 유저를 추가할 수 없다.")
    @Test
    void cannotAddUser() {
        List<User> users = Arrays.asList(new User(1, "encoded", "BLACK", 1),
                new User(2, "enco", "WHITE", 1));
        given(userDAO.findAllByRoomId(1)).willReturn(users);

        assertThatCode(() -> userService.addUserIntoRoom(1, "pass"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 꽉 찬 방입니다.");
        verify(userDAO, times(1)).findAllByRoomId(1);
    }

    @DisplayName("인자로 주어진 이동 요청 유저의 정보가 방에 있는 유저들 중 하나라도 일치하지 않으면 올바르지 않은 차례다.")
    @Test
    void cannotMove() {
        List<User> users = Arrays.asList(new User(1, "encoded", "BLACK", 1),
                new User(2, "enco", "WHITE", 1));
        given(userDAO.findAllByRoomId(1)).willReturn(users);

        assertThatCode(() -> userService.validateUserTurn(1, "encoded", TeamType.WHITE))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("현재 차례가 아닙니다.");
        verify(userDAO, times(1)).findAllByRoomId(1);
    }

    @DisplayName("혼자서는 게임을 진행할 수 없다.")
    @Test
    void cannotPlayAlone() {
        List<User> users = Arrays.asList(new User(1, "encoded", "BLACK", 1));
        given(userDAO.findAllByRoomId(1)).willReturn(users);

        assertThatCode(() -> userService.validateUserTurn(1, "encoded", TeamType.WHITE))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("혼자서는 플레이할 수 없습니다.");
        verify(userDAO, times(1)).findAllByRoomId(1);
    }

    @DisplayName("주어진 ID 및 패스워드가 일치한 유저가 없으면 삭제 불가다.")
    @Test
    void cannotDeleteUser() {
        List<User> users = Arrays.asList(new User(1, "pass", "black", 1),
                new User(2, "pass2", "black", 1));
        given(userDAO.findAllByRoomId(1)).willReturn(users);

        assertThatCode(() -> userService.deleteUser(1, "kakak1"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("삭제할 유저가 존재하지 않습니다.");
        verify(userDAO, times(1)).findAllByRoomId(1);
    }
}
