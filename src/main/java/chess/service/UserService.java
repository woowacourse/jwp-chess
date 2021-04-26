package chess.service;

import chess.dao.UserDao;
import chess.dao.dto.UserDto;
import chess.dto.user.UserRequestDto;
import chess.dto.user.UserResponseDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final UserDao userDao;

    public UserService(final UserDao userDao) {
        this.userDao = userDao;
    }

    public long add(final UserRequestDto userRequestDto) {
        return userDao.insert(userRequestDto.toUserDto());
    }

    public UserResponseDto findUserByName(final String name) {
        final UserDto userDto = userDao.selectByName(name);
        return UserResponseDto.from(userDto);
    }

    public UserResponseDto findUserById(final long id) {
        return UserResponseDto.from(userDao.selectById(id));
    }

}
