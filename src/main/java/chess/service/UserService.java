package chess.service;

import chess.dao.UserDao;
import chess.dto.UserResponseDto;
import chess.entity.UserEntity;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public long add(UserEntity userEntity) {
        return userDao.insert(userEntity);
    }

    public UserResponseDto findUserByName(String name) {
        UserEntity userEntity = userDao.selectByName(name);
        return UserResponseDto.from(userEntity);
    }
}
