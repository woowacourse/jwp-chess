package wooteco.chess.repository;

import org.springframework.data.repository.CrudRepository;
import wooteco.chess.domain.player.User;

public interface UserRepository extends CrudRepository<User, Long> {
}
