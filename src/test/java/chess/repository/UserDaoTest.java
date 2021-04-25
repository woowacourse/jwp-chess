package chess.repository;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.web.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserDaoTest {

    private static final int DEFAULT_USER_ID = 1;
    private static final String DEFAULT_USER_NAME = "userDefault";

    @Autowired
    private UserDao userDao;

    @BeforeAll
    public void setUserData() {
        User user = new User(DEFAULT_USER_NAME);
        userDao.addUser(user);
    }

    @Test
    @DisplayName("유저 등록 테스트")
    public void addUserTest() {
        User user = new User("user1");
        int userId = userDao.addUser(user);

        assertThat(userId).isEqualTo(2);
    }

    @Test
    @DisplayName("아이디로 유저 찾기 테스트")
    public void findUserByIdTest() {
        Optional<User> userById = userDao.findUserById(DEFAULT_USER_ID);

        assertThat(userById.get().getName()).isEqualTo(DEFAULT_USER_NAME);
    }

    @Test
    @DisplayName("아이디로 유저 찾기 예외 테스트")
    public void findUserByIdExceptionTest() {
        int isNotExistUserId = 5;
        Optional<User> userById = userDao.findUserById(isNotExistUserId);

        assertThat(userById.isPresent()).isFalse();
    }

    @Test
    @DisplayName("이름으로 유저 찾기 테스트")
    public void findUserByNameTest() {
        Optional<User> userByName = userDao.findByName(DEFAULT_USER_NAME);

        assertThat(userByName.get().getUserId()).isEqualTo(DEFAULT_USER_ID);

    }

    @Test
    @DisplayName("이름으로 유저 찾기 예외 테스트")
    public void findUserByNameExceptionTest() {
        String isNotExistUserName = "I'm not exist";
        Optional<User> userByName= userDao.findByName(isNotExistUserName);

        assertThat(userByName.isPresent()).isFalse();
    }
}