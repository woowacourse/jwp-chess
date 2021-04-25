package chess.dao;

import chess.entity.User;

import java.util.Optional;

public interface UserDao {

    void save(final User user);

    Optional<User> findByName(String name);
}
