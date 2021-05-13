package chess.domain.repository.user;

import chess.domain.user.User;

import java.util.Optional;

public interface UserRepository {

    Long save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByName(String name);

    Optional<User> findByRoomIdAndName(Long roomId, String userName);
}
