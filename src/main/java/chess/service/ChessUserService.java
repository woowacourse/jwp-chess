package chess.service;

import chess.dao.UserDao;
import chess.domain.User;
import dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChessUserService {

    @Autowired
    private final UserDao userDao;

    public ChessUserService(final UserDao userDao) {
        this.userDao = userDao;
    }


    public void create(final String name, final String pw) {
        userDao.create(name, pw);

    }

    public UserDto login(final String name, final String pw) {
        User user = userDao.findByName(name);
        if (!user.checkPassword(pw)) {
            throw  new IllegalArgumentException();
        }

        return UserDto.toResponse(user);
    }

    public void exit(final String name) {
        User user = userDao.findByName(name);
        if (user.inGame()) {
            userDao.setRoomId(null, name);
        }
    }
}