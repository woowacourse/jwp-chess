package chess.mysql;

import chess.chessgame.domain.room.game.board.piece.attribute.Color;
import chess.chessgame.domain.room.user.User;
import chess.chessgame.repository.UserRepository;
import chess.mysql.dao.UserDao;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static final int TEMPORARY_ID = 0;

    private final UserDao userDao;

    public UserRepositoryImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User createUser(Color color, String password) {
        return userDao.insertUser(new User(TEMPORARY_ID, color, password));
    }

    @Override
    public User matchPasswordUser(long roomId, String password) {
        return userDao.findByRoomId(roomId).stream()
                .filter(user -> user.getPassword().equals(password))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("해당 password와 일치하는 유저가 없습니다."));
    }

    @Override
    public User findByUserId(long userId) {
        return null;
    }

    @Override
    public void deleteUser(User user) {

    }
}
