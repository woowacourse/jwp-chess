package chess.service;

import chess.dao.UserDao;
import chess.dto.UserResponseDto;
import chess.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public long add(User user) {
        return userDao.insert(user);
    }

    public UserResponseDto findUserByName(String name) {
        User user = userDao.selectByName(name);
        return UserResponseDto.from(user);
    }
}
