package chess.mysql;

import chess.chessgame.domain.room.game.board.piece.attribute.Color;
import chess.chessgame.domain.room.user.User;
import chess.chessgame.repository.UserRepository;
import chess.mysql.dao.UserDao;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static final int TEMPORARY_ID = 0;

    private final UserDao userDao;

    public UserRepositoryImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createUser(String password, Color color) {
        return userDao.insertUser(new User(TEMPORARY_ID, color, password));
    }

    @Override
    public List<User> findByGameId(long gameId) {
        return null;
    }

    @Override
    public User findByUserId(long userId) {
        return null;
    }

    @Override
    public void deleteUser(User user) {

    }
}
