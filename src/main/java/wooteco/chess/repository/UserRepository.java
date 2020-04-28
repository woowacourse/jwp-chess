package wooteco.chess.repository;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wooteco.chess.domain.player.User;

public interface UserRepository extends CrudRepository<User, Long> {

//    @Modifying
//    @Query("INSERT INTO user(name) VALUES (:name)")
//    void addUserByName(@Param("name") String name);
}
