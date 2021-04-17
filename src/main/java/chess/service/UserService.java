package chess.service;

import chess.dao.UserDao;
import chess.dto.UserRequestDto;
import chess.dto.UserResponseDto;
import chess.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public long add(UserRequestDto userRequestDto) {
        return userDao.insert(userRequestDto.toEntity());
    }

    public UserResponseDto findUserByName(String name) {
        User user = userDao.selectByName(name);
        return UserResponseDto.from(user);
    }

    public UserResponseDto findUserById(long id) {
        return UserResponseDto.from(userDao.selectById(id));
    }

}
