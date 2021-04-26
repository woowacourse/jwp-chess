package chess.service;

import chess.dao.UserDao;
import chess.domain.user.User;
import chess.dto.user.UserRequestDto;
import chess.dto.user.UserResponseDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final UserDao userDao;

    public UserService(final UserDao userDao) {
        this.userDao = userDao;
    }

    public long add(final  UserRequestDto userRequestDto) {
        return userDao.insert(userRequestDto.toEntity());
    }

    public UserResponseDto findUserByName(final String name) {
        final User user = userDao.selectByName(name);
        return UserResponseDto.from(user);
    }

    public UserResponseDto findUserById(final long id) {
        return UserResponseDto.from(userDao.selectById(id));
    }

}
