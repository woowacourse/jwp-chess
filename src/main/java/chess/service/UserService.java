package chess.service;

import chess.dao.UserDao;
import chess.domain.user.User;
import java.util.List;

public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void save(User user) {
        userDao.save(user);
    }

    public User findById(String id) {
        return userDao.findByName(id);
    }

    public List<User> findAll(String id) {
        return userDao.findAll();
    }
}
