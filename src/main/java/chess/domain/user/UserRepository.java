package chess.domain.user;

import chess.dao.UserDao;
import chess.dao.dto.UserDto;
import java.util.Objects;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    private final UserDao userDao;

    public UserRepository(final UserDao userDao) {
        this.userDao = userDao;
    }

    public long add(final User user) {
        return userDao.insert(UserDto.from(user));
    }

    public User findByName(final String name) {
        final UserDto userDto = userDao.selectByName(name);
        return userDto.toEntity();
    }

    public User findById(final Long id) {
        if (Objects.isNull(id)) {
            return User.empty();
        }
        return userDao.selectById(id).toEntity();
    }

}
