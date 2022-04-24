package chess.dao;

import static org.assertj.core.api.Assertions.assertThat;

import chess.domain.user.User;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DbUserDaoTest {

    private static final DbUserDao userDao = new DbUserDao();

    @BeforeEach
    void setUp() {
        userDao.deleteAll();
    }

    @AfterAll
    static void afterAll() {
        userDao.deleteAll();
    }

    @Test
    void saveAndFindById() {
        User user = new User("philz");
        userDao.save(user);
        User findUser = userDao.findByName("philz");
        assertThat(findUser).isEqualTo(user);
    }

    @Test
    void findAll() {
        userDao.save(new User("user a"));
        userDao.save(new User("user b"));
        List<User> Users = userDao.findAll();
        assertThat(Users.size()).isEqualTo(2);
    }

    @Test
    void deleteById() {
        userDao.save(new User("user a"));
        userDao.save(new User("user b"));
        userDao.deleteByName("user a");
        List<User> Users = userDao.findAll();
        assertThat(Users.size()).isEqualTo(1);
    }
}