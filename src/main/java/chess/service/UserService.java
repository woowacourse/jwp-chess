package chess.service;

import chess.dao.UserDao;
import chess.entity.User;
import chess.service.dto.UserSaveRequestDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public void save(final UserSaveRequestDto requestDto) {
        User user = requestDto.toEntity();
        userDao.save(user);
    }
}
