package chess.service;

import chess.dao.UserDao;
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
}
