package chess.dao;

import chess.domain.user.User;
import java.util.List;

public interface UserDao {

    void save(User user);

    User findByName(String id);

    List<User> findAll();

    void deleteByName(String id);

    void deleteAll();
}
